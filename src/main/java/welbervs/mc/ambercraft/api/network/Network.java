package welbervs.mc.ambercraft.api.network;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A connected graph of {@link NetworkComponent}s. Every registered component belongs to exactly one network.
 * <p>
 * Networks are created, merged and split by the {@link NetworkManager}; they are runtime only (rebuilt when chunks load).
 * {@link #getVersion()} changes every time the topology changes, so a solver can cheaply detect when it must rebuild.
 */
public final class Network
{
    private static final AtomicLong NEXT_ID = new AtomicLong();

    private final long id = NEXT_ID.incrementAndGet();
    private final ServerLevel level;
    final ReferenceOpenHashSet<NetworkComponent> nodes = new ReferenceOpenHashSet<>();
    private final Set<NetworkComponent> nodesView = Collections.unmodifiableSet(nodes);
    private int version;

    Network(ServerLevel level)
    {
        this.level = level;
    }

    /// Unique id during the session (not persisted).
    public long getId()
    {
        return id;
    }

    public ServerLevel getLevel()
    {
        return level;
    }

    /// Read-only view of all components in the network.
    public Set<NetworkComponent> getNodes()
    {
        return nodesView;
    }

    public int size()
    {
        return nodes.size();
    }

    /// Incremented each time a node or connection is added/removed.
    public int getVersion()
    {
        return version;
    }

    void markChanged()
    {
        version++;
    }

    /// Builds a list with every connection of the network (each one listed once).
    public List<NetworkConnection> collectConnections()
    {
        List<NetworkConnection> list = new ArrayList<>();
        for (NetworkComponent node : nodes)
            for (NetworkConnection connection : node.connections)
                if (connection.getA() == node)
                    list.add(connection);
        return list;
    }

    /// All nodes of the given type.
    @SuppressWarnings("unchecked")
    public <T extends NetworkComponent> List<T> getNodesOfType(NetworkComponentType<T> type)
    {
        List<T> list = new ArrayList<>();
        for (NetworkComponent node : nodes)
            if (node.getType() == type)
                list.add((T) node);
        return list;
    }

    @Override
    public String toString()
    {
        return "Network#" + id + "[" + nodes.size() + " nodes]";
    }
}
