package welbervs.mc.ambercraft.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import welbervs.mc.ambercraft.api.component.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A {@link Component} that is a node of a {@link Network} graph.
 * <p>
 * The <b>identity</b> of the instance is the identity of the node: a capability provider must return the same instance
 * every time it is asked (store it in the BlockEntity, in an attachment, etc...). The faces where the component is
 * exposed are discovered by querying the {@code ComponentSourceCapability} with a {@code Context} for each {@link Direction}.
 * <p>
 * The class is open: extend it to store the data your solver needs, and use a {@link NetworkComponentType} for the behaviour.
 * The fields related to the graph are runtime only and are managed by the {@link NetworkManager}.
 */
public class NetworkComponent implements Component
{
    private final NetworkComponentType<?> type;

    // ------------- runtime state, managed by the NetworkManager -------------
    @Nullable Network network;
    @Nullable BlockPos pos;
    /// Bitmask of the exposed sides, indexed by {@link Direction#ordinal()}.
    int exposedSides;
    /// true when the component was discovered through the capability, false when added manually.
    boolean fromCapability;
    final ArrayList<NetworkConnection> connections = new ArrayList<>(6);
    private final List<NetworkConnection> connectionsView = Collections.unmodifiableList(connections);

    public NetworkComponent(@NotNull NetworkComponentType<?> type)
    {
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    public @NotNull NetworkComponentType<?> getType()
    {
        return type;
    }

    public NetworkDomain getDomain()
    {
        return type.getDomain();
    }

    /// The network of this component, null if it isn't registered in a {@link NetworkManager}.
    public @Nullable Network getNetwork()
    {
        return network;
    }

    public boolean isInNetwork()
    {
        return network != null;
    }

    /// The position where the component is registered, null if it isn't registered.
    public @Nullable BlockPos getPos()
    {
        return pos;
    }

    public boolean isExposedOn(Direction side)
    {
        return (exposedSides & (1 << side.ordinal())) != 0;
    }

    /// Bitmask of the exposed sides, indexed by {@link Direction#ordinal()}.
    public int getExposedSides()
    {
        return exposedSides;
    }

    /// Read-only view of the connections of this component.
    public List<NetworkConnection> getConnections()
    {
        return connectionsView;
    }

    public @Nullable NetworkConnection getConnectionTo(NetworkComponent other)
    {
        for (NetworkConnection connection : connections)
            if (connection.other(this) == other)
                return connection;
        return null;
    }

    public boolean isConnectedTo(NetworkComponent other)
    {
        return getConnectionTo(other) != null;
    }

    /// Shortcut to {@link NetworkComponentType#canConnect(NetworkComponent, NetworkComponent, Direction)}.
    public boolean canConnectTo(NetworkComponent other, @Nullable Direction side)
    {
        return type.canConnect(this, other, side);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + type.id + " @ " + (pos == null ? "unregistered" : pos.toShortString()) + "]";
    }
}
