package welbervs.mc.ambercraft.api.network;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * An undirected edge between two {@link NetworkComponent}s.
 * <p>
 * Automatic connections are created/removed by the {@link NetworkManager} based on block adjacency.
 * Manual connections are created through {@link NetworkManager#connect(NetworkComponent, NetworkComponent)} and are
 * never touched by the automatic system.
 */
public final class NetworkConnection
{
    private final NetworkComponent a;
    private final NetworkComponent b;
    private final @Nullable Direction side;
    private final boolean automatic;

    NetworkConnection(NetworkComponent a, NetworkComponent b, @Nullable Direction side, boolean automatic)
    {
        this.a = a;
        this.b = b;
        this.side = side;
        this.automatic = automatic;
    }

    public NetworkComponent getA()
    {
        return a;
    }

    public NetworkComponent getB()
    {
        return b;
    }

    /// The component on the other end of the connection.
    public NetworkComponent other(NetworkComponent component)
    {
        return component == a ? b : a;
    }

    /// The side of {@link #getA()} where the connection is, null for manual connections.
    public @Nullable Direction getSide()
    {
        return side;
    }

    /// The side of {@code component} where the connection is, null for manual connections.
    public @Nullable Direction sideFrom(NetworkComponent component)
    {
        if (side == null)
            return null;
        return component == a ? side : side.getOpposite();
    }

    public boolean isAutomatic()
    {
        return automatic;
    }

    @Override
    public String toString()
    {
        return "NetworkConnection[" + a.getType().id + " <-" + side + "-> " + b.getType().id + (automatic ? "" : ", manual") + "]";
    }
}
