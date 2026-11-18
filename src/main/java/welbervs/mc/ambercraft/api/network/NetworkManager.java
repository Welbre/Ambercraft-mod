package welbervs.mc.ambercraft.api.network;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.api.registry.CapabilitiesRegister;
import welbervs.mc.ambercraft.api.registry.DataAttachmentRegister;
import welbervs.mc.ambercraft.core.Ambercraft;

import java.util.*;

/**
 * Owns every {@link Network} of a {@link ServerLevel} and keeps the graph in sync with the world.
 * <p>
 * <b>How it works:</b>
 * <ul>
 *     <li>Every block change marks the position as dirty (see {@code ServerLevelMixin}). At the end of the level tick
 *     each dirty position is refreshed: the {@code ComponentSourceCapability} is queried once per {@link Direction},
 *     the {@link NetworkComponent}s found are added, the ones that vanished are removed and the connections with the
 *     6 neighbours are recalculated.</li>
 *     <li>The positions that have components are saved in a chunk attachment, so they are refreshed again when the
 *     chunk loads. When a chunk unloads, its components are removed from the networks.</li>
 *     <li>Mods can also add components manually ({@link #addComponent}) and create non-adjacent connections
 *     ({@link #connect}). Manual components aren't saved: the owner must add them again after a chunk load.</li>
 * </ul>
 * Everything must be called on the server thread.
 */
public final class NetworkManager
{
    private static final Map<ServerLevel, NetworkManager> MANAGERS = new IdentityHashMap<>();
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final ComponentSourceCapability.Context[] SIDE_CONTEXTS = new ComponentSourceCapability.Context[DIRECTIONS.length];
    /// How many ticks a dirty position waits for its chunk to finish loading before being dropped.
    private static final int MAX_DIRTY_WAIT = 200;

    static
    {
        for (Direction direction : DIRECTIONS)
            SIDE_CONTEXTS[direction.ordinal()] = new ComponentSourceCapability.Context(direction, null);
    }

    private final ServerLevel level;
    private final Long2ObjectOpenHashMap<List<NetworkComponent>> nodesByPos = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<LongOpenHashSet> posByChunk = new Long2ObjectOpenHashMap<>();
    /// dirty position -> how many ticks it's waiting for the chunk to load.
    private final Long2IntLinkedOpenHashMap dirty = new Long2IntLinkedOpenHashMap();
    private final ReferenceOpenHashSet<Network> networks = new ReferenceOpenHashSet<>();
    private final Set<Network> networksView = Collections.unmodifiableSet(networks);

    private NetworkManager(ServerLevel level)
    {
        this.level = level;
    }

    /// Returns the manager of the level, creating it if needed.
    public static NetworkManager get(@NotNull ServerLevel level)
    {
        return MANAGERS.computeIfAbsent(level, NetworkManager::new);
    }

    /// Returns the manager of the level or null if it doesn't exist (or the level is client side).
    public static @Nullable NetworkManager getIfPresent(@NotNull Level level)
    {
        return MANAGERS.get(level);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------Queries-----------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    public ServerLevel getLevel()
    {
        return level;
    }

    /// Read-only view of all networks in the level.
    public Set<Network> getNetworks()
    {
        return networksView;
    }

    /// The components registered at the position (read-only).
    public List<NetworkComponent> getComponentsAt(BlockPos pos)
    {
        List<NetworkComponent> list = nodesByPos.get(pos.asLong());
        return list == null ? List.of() : Collections.unmodifiableList(list);
    }

    public boolean hasComponentsAt(BlockPos pos)
    {
        return nodesByPos.containsKey(pos.asLong());
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------Public API--------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Schedules a refresh of the position at the end of the tick. Call it when a block changes the components it exposes
     * without changing its BlockState (block changes are detected automatically).
     */
    public void markDirty(BlockPos pos)
    {
        dirty.putIfAbsent(pos.asLong(), 0);
    }

    /**
     * Manually adds a component to the graph and connects it with the adjacent components.
     * Manual components are never removed by the automatic system, use {@link #removeComponent(NetworkComponent)}.
     *
     * @param exposedSides The sides where the component can be connected automatically, empty for none.
     * @throws IllegalStateException if the component is already in a network.
     */
    public void addComponent(BlockPos pos, NetworkComponent component, Direction... exposedSides)
    {
        int mask = 0;
        for (Direction side : exposedSides)
            mask |= 1 << side.ordinal();
        addComponent(pos, component, mask);
    }

    /// @see #addComponent(BlockPos, NetworkComponent, Direction...)
    public void addComponent(BlockPos pos, NetworkComponent component, int exposedSidesMask)
    {
        if (component.network != null)
            throw new IllegalStateException(component + " is already in a network");
        register(pos.immutable(), component, exposedSidesMask, false);
        updateAutomaticConnections(component.pos);
    }

    /**
     * Removes the component from its network (splitting it if needed).
     *
     * @return false if the component isn't registered in this manager.
     */
    public boolean removeComponent(NetworkComponent component)
    {
        if (!owns(component))
            return false;
        unregister(List.of(component));
        return true;
    }

    /**
     * Creates a manual connection between two registered components (they don't need to be adjacent). Both
     * {@link NetworkComponentType#canConnect} must accept it, with {@code side == null}.
     *
     * @return the connection (or the existing one), null if one of the components refused it.
     * @throws IllegalArgumentException if one of the components isn't registered in this manager.
     */
    public @Nullable NetworkConnection connect(NetworkComponent a, NetworkComponent b)
    {
        if (!owns(a) || !owns(b))
            throw new IllegalArgumentException("Both components must be registered in this NetworkManager");
        if (a == b)
            return null;

        NetworkConnection existing = a.getConnectionTo(b);
        if (existing != null)
            return existing;

        if (!a.getType().canConnect(a, b, null) || !b.getType().canConnect(b, a, null))
            return null;

        return link(a, b, null, false);
    }

    /**
     * Removes the connection between the two components (automatic or manual). Note that an automatic connection will be
     * created again in the next refresh of one of the positions if the rules still accept it.
     *
     * @return false if they weren't connected.
     */
    public boolean disconnect(NetworkComponent a, NetworkComponent b)
    {
        NetworkConnection connection = a.getConnectionTo(b);
        if (connection == null)
            return false;
        removeConnection(connection);
        return true;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------World hooks (internal)--------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    /// Called by the mixin every time a BlockState changes in a ServerLevel.
    @ApiStatus.Internal
    public static void onBlockStateChanged(ServerLevel level, BlockPos pos, BlockState oldState, BlockState newState)
    {
        if (!level.getServer().isSameThread())
        {
            BlockPos immutable = pos.immutable();
            level.getServer().execute(() -> onBlockStateChanged(level, immutable, oldState, newState));
            return;
        }

        NetworkManager manager = get(level);
        // Same block: only matters if the position already has components (the state may change the exposed sides).
        if (oldState.getBlock() != newState.getBlock() || manager.hasComponentsAt(pos))
            manager.markDirty(pos);
    }

    @ApiStatus.Internal
    public void tick()
    {
        if (dirty.isEmpty())
            return;

        long[] positions = dirty.keySet().toLongArray();
        int[] waits = dirty.values().toIntArray();
        dirty.clear();

        for (int i = 0; i < positions.length; i++)
        {
            if (!refresh(positions[i]) && waits[i] < MAX_DIRTY_WAIT)
                dirty.putIfAbsent(positions[i], waits[i] + 1);//chunk not ready yet, try again in the next tick
        }
    }

    @ApiStatus.Internal
    public void onChunkLoaded(ChunkAccess chunk)
    {
        LongOpenHashSet saved = chunk.getExistingDataOrNull(DataAttachmentRegister.NETWORK_POSITIONS.get());
        if (saved == null)
            return;
        for (long pos : saved)
            dirty.putIfAbsent(pos, 0);
    }

    @ApiStatus.Internal
    public void onChunkUnloaded(ChunkPos chunkPos)
    {
        long chunkKey = chunkPos.toLong();

        // drop pending refreshes of the chunk
        for (Iterator<Long2IntMap.Entry> it = dirty.long2IntEntrySet().fastIterator(); it.hasNext(); )
            if (ChunkPos.asLong(BlockPos.of(it.next().getLongKey())) == chunkKey)
                it.remove();

        LongOpenHashSet positions = posByChunk.get(chunkKey);
        if (positions == null)
            return;

        List<NetworkComponent> toRemove = new ArrayList<>();
        for (long pos : positions)
        {
            List<NetworkComponent> nodes = nodesByPos.get(pos);
            if (nodes != null)
                toRemove.addAll(nodes);
        }
        unregister(toRemove);
    }

    /// Called when the level unloads.
    @ApiStatus.Internal
    public static void onLevelUnloaded(Level level)
    {
        NetworkManager manager = MANAGERS.remove(level);
        if (manager == null)
            return;

        // Reset the runtime state, so the instances can be reused safely.
        for (List<NetworkComponent> nodes : manager.nodesByPos.values())
            for (NetworkComponent node : nodes)
            {
                node.network = null;
                node.pos = null;
                node.exposedSides = 0;
                node.connections.clear();
            }
        manager.nodesByPos.clear();
        manager.posByChunk.clear();
        manager.networks.clear();
        manager.dirty.clear();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------Refresh-----------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Synchronizes the components of the position with the capability.
     *
     * @return false if the chunk isn't loaded yet.
     */
    private boolean refresh(long posKey)
    {
        BlockPos pos = BlockPos.of(posKey);
        if (!level.isLoaded(pos))
            return false;

        // 1. Discover the components and the sides where each one is exposed.
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Reference2IntLinkedOpenHashMap<NetworkComponent> found = new Reference2IntLinkedOpenHashMap<>();
        for (Direction side : DIRECTIONS)
        {
            ComponentSourceCapability source = level.getCapability(
                    CapabilitiesRegister.COMPONENT_SOURCE_CAPABILITY, pos, state, blockEntity, SIDE_CONTEXTS[side.ordinal()]);
            if (source == null)
                continue;
            for (Component component : source.components())
                if (component instanceof NetworkComponent node)
                    found.put(node, found.getInt(node) | (1 << side.ordinal()));
        }

        // 2. Remove the components that aren't exposed anymore (only the ones discovered by the capability).
        List<NetworkComponent> current = nodesByPos.get(posKey);
        if (current != null)
        {
            List<NetworkComponent> removed = new ArrayList<>();
            for (NetworkComponent node : current)
                if (node.fromCapability && !found.containsKey(node))
                    removed.add(node);
            unregister(removed);
        }

        // 3. Register the new ones and update the sides of the old ones.
        boolean hasCapabilityNodes = false;
        for (Reference2IntMap.Entry<NetworkComponent> entry : found.reference2IntEntrySet())
        {
            NetworkComponent node = entry.getKey();
            if (node.network == null)
                register(pos, node, entry.getIntValue(), true);
            else if (owns(node) && pos.equals(node.pos))
                node.exposedSides = entry.getIntValue();
            else
            {
                Ambercraft.LOGGER.warn("{} is exposed at {} but it's already registered at {}, ignoring it. Components must be unique per position.", node, pos, node.pos);
                continue;
            }
            hasCapabilityNodes = true;
        }

        // 4. Remember (or forget) the position, so it's refreshed when the chunk loads.
        updateChunkIndex(pos, hasCapabilityNodes);

        // 5. Recalculate the connections with the neighbours.
        updateAutomaticConnections(pos);
        return true;
    }

    /// Recalculates the automatic connections between the components at {@code pos} and its 6 neighbours.
    private void updateAutomaticConnections(BlockPos pos)
    {
        List<NetworkComponent> here = nodesByPos.get(pos.asLong());
        if (here == null)
            return;

        for (Direction side : DIRECTIONS)
        {
            BlockPos neighbourPos = pos.relative(side);
            List<NetworkComponent> there = nodesByPos.get(neighbourPos.asLong());

            for (NetworkComponent a : List.copyOf(here))
            {
                if (a.network == null)
                    continue;

                // remove automatic connections through this side that aren't valid anymore
                for (NetworkConnection connection : List.copyOf(a.connections))
                    if (connection.isAutomatic() && connection.sideFrom(a) == side && !isStillValid(a, connection.other(a), side))
                        removeConnection(connection);

                if (there == null || !a.isExposedOn(side))
                    continue;

                // create the new ones
                Direction opposite = side.getOpposite();
                for (NetworkComponent b : List.copyOf(there))
                {
                    if (a.network == null)
                        break;
                    if (b.network == null || !b.isExposedOn(opposite) || a.isConnectedTo(b))
                        continue;
                    if (rulesAccept(a, b, side) && !NeoForge.EVENT_BUS.post(new ComponentConnectEvent(level, a, b, side)).isCanceled())
                        link(a, b, side, true);
                }
            }
        }
    }

    private static boolean rulesAccept(NetworkComponent a, NetworkComponent b, Direction side)
    {
        return a.getType().canConnect(a, b, side) && b.getType().canConnect(b, a, side.getOpposite());
    }

    private static boolean isStillValid(NetworkComponent a, NetworkComponent b, Direction side)
    {
        return a.network != null && b.network != null
                && a.isExposedOn(side) && b.isExposedOn(side.getOpposite())
                && b.pos != null && a.pos != null && b.pos.equals(a.pos.relative(side))
                && rulesAccept(a, b, side);
    }

    private void updateChunkIndex(BlockPos pos, boolean hasComponents)
    {
        LevelChunk chunk = level.getChunkAt(pos);
        var type = DataAttachmentRegister.NETWORK_POSITIONS.get();
        LongOpenHashSet saved = chunk.getExistingDataOrNull(type);
        if (hasComponents)
        {
            if (saved == null)
            {
                saved = new LongOpenHashSet();
                chunk.setData(type, saved);
            }
            if (saved.add(pos.asLong()))
                chunk.setUnsaved(true);
        }
        else if (saved != null && saved.remove(pos.asLong()))
        {
            if (saved.isEmpty())
                chunk.removeData(type);
            else
                chunk.setUnsaved(true);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------Graph operations--------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------

    private boolean owns(NetworkComponent component)
    {
        return component.network != null && component.network.getLevel() == level;
    }

    /// Adds the component to the indexes inside a new network with only itself.
    private void register(BlockPos pos, NetworkComponent component, int exposedSides, boolean fromCapability)
    {
        component.pos = pos;
        component.exposedSides = exposedSides;
        component.fromCapability = fromCapability;

        long posKey = pos.asLong();
        nodesByPos.computeIfAbsent(posKey, k -> new ArrayList<>(1)).add(component);
        posByChunk.computeIfAbsent(ChunkPos.asLong(pos), k -> new LongOpenHashSet()).add(posKey);

        Network network = new Network(level);
        network.nodes.add(component);
        networks.add(network);
        component.network = network;
        component.getType().onNetworkChanged(component, null, network);
    }

    /// Removes all the components at once, splitting each affected network only once.
    private void unregister(Collection<NetworkComponent> components)
    {
        if (components.isEmpty())
            return;

        ReferenceOpenHashSet<NetworkComponent> removing = new ReferenceOpenHashSet<>(components);
        Reference2ObjectOpenHashMap<Network, List<NetworkComponent>> seedsByNetwork = new Reference2ObjectOpenHashMap<>();

        for (NetworkComponent component : removing)
        {
            Network network = component.network;
            if (network == null)
                continue;
            List<NetworkComponent> seeds = seedsByNetwork.computeIfAbsent(network, n -> new ArrayList<>());

            for (NetworkConnection connection : List.copyOf(component.connections))
            {
                NetworkComponent other = connection.other(component);
                unlink(connection);
                if (!removing.contains(other))
                    seeds.add(other);
            }

            network.nodes.remove(component);
            removeFromIndex(component);
            component.network = null;
            component.getType().onNetworkChanged(component, network, null);
            component.pos = null;
            component.exposedSides = 0;
        }

        for (var entry : seedsByNetwork.entrySet())
        {
            Network network = entry.getKey();
            if (network.nodes.isEmpty())
                networks.remove(network);
            else
            {
                network.markChanged();
                splitIfNeeded(network, entry.getValue());
            }
        }
    }

    private void removeFromIndex(NetworkComponent component)
    {
        BlockPos pos = component.pos;
        if (pos == null)
            return;

        long posKey = pos.asLong();
        List<NetworkComponent> list = nodesByPos.get(posKey);
        if (list == null)
            return;
        list.remove(component);
        if (!list.isEmpty())
            return;

        nodesByPos.remove(posKey);
        long chunkKey = ChunkPos.asLong(pos);
        LongOpenHashSet chunkPositions = posByChunk.get(chunkKey);
        if (chunkPositions != null && chunkPositions.remove(posKey) && chunkPositions.isEmpty())
            posByChunk.remove(chunkKey);
    }

    /// Creates the connection and merges the networks.
    private NetworkConnection link(NetworkComponent a, NetworkComponent b, @Nullable Direction side, boolean automatic)
    {
        NetworkConnection connection = new NetworkConnection(a, b, side, automatic);
        a.connections.add(connection);
        b.connections.add(connection);
        merge(a.network, b.network);
        a.getType().onConnected(a, connection);
        b.getType().onConnected(b, connection);
        return connection;
    }

    /// Removes the connection from both components, without checking if the network split.
    private void unlink(NetworkConnection connection)
    {
        NetworkComponent a = connection.getA();
        NetworkComponent b = connection.getB();
        a.connections.remove(connection);
        b.connections.remove(connection);
        a.getType().onDisconnected(a, connection);
        b.getType().onDisconnected(b, connection);
    }

    private void removeConnection(NetworkConnection connection)
    {
        unlink(connection);
        Network network = connection.getA().network;
        if (network == null)
            return;
        network.markChanged();
        splitIfNeeded(network, List.of(connection.getA(), connection.getB()));
    }

    /// Moves the nodes of the smaller network to the bigger one.
    private void merge(Network x, Network y)
    {
        if (x == y)
        {
            x.markChanged();
            return;
        }
        Network big = x.size() >= y.size() ? x : y;
        Network small = big == x ? y : x;

        NetworkComponent[] moved = small.nodes.toArray(new NetworkComponent[0]);
        for (NetworkComponent node : moved)
        {
            node.network = big;
            big.nodes.add(node);
        }
        small.nodes.clear();
        networks.remove(small);
        big.markChanged();

        for (NetworkComponent node : moved)
            node.getType().onNetworkChanged(node, small, big);
    }

    /**
     * Checks if the seeds (nodes that lost connections) are still connected. If they aren't, the network is split: the
     * biggest part stays in the original network and each other part becomes a new network.
     */
    private void splitIfNeeded(Network network, List<NetworkComponent> seeds)
    {
        ReferenceOpenHashSet<NetworkComponent> pending = new ReferenceOpenHashSet<>();
        for (NetworkComponent seed : seeds)
            if (seed.network == network)
                pending.add(seed);
        if (pending.size() <= 1)
            return;

        // First BFS stops as soon as it finds all seeds, the common case (no split) is cheap.
        NetworkComponent first = pending.iterator().next();
        ReferenceOpenHashSet<NetworkComponent> firstGroup = new ReferenceOpenHashSet<>();
        if (bfs(first, firstGroup, pending))
            return;

        List<ReferenceOpenHashSet<NetworkComponent>> groups = new ArrayList<>();
        groups.add(firstGroup);
        for (NetworkComponent seed : pending)
        {
            boolean visited = false;
            for (ReferenceOpenHashSet<NetworkComponent> group : groups)
                if (group.contains(seed))
                {
                    visited = true;
                    break;
                }
            if (visited)
                continue;
            ReferenceOpenHashSet<NetworkComponent> group = new ReferenceOpenHashSet<>();
            bfs(seed, group, null);
            groups.add(group);
        }

        if (groups.size() <= 1)
            return;

        ReferenceOpenHashSet<NetworkComponent> biggest = Collections.max(groups, Comparator.comparingInt(Set::size));
        List<Runnable> callbacks = new ArrayList<>();
        for (ReferenceOpenHashSet<NetworkComponent> group : groups)
        {
            if (group == biggest)
                continue;
            Network created = new Network(level);
            networks.add(created);
            for (NetworkComponent node : group)
            {
                network.nodes.remove(node);
                created.nodes.add(node);
                node.network = created;
                callbacks.add(() -> node.getType().onNetworkChanged(node, network, created));
            }
        }
        network.markChanged();
        callbacks.forEach(Runnable::run);
    }

    /**
     * Fills {@code visited} with all nodes reachable from {@code start}.
     *
     * @param stopWhenFound if not null, the search stops when all these nodes were found.
     * @return true if it stopped because all nodes of {@code stopWhenFound} were found.
     */
    private static boolean bfs(NetworkComponent start, ReferenceOpenHashSet<NetworkComponent> visited, @Nullable Set<NetworkComponent> stopWhenFound)
    {
        int remaining = stopWhenFound == null ? -1 : stopWhenFound.size();
        ArrayDeque<NetworkComponent> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);
        if (stopWhenFound != null && stopWhenFound.contains(start) && --remaining == 0)
            return true;

        while (!queue.isEmpty())
        {
            NetworkComponent node = queue.poll();
            for (NetworkConnection connection : node.connections)
            {
                NetworkComponent other = connection.other(node);
                if (!visited.add(other))
                    continue;
                if (stopWhenFound != null && stopWhenFound.contains(other) && --remaining == 0)
                    return true;
                queue.add(other);
            }
        }
        return false;
    }
}
