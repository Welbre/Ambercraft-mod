package welbervs.mc.ambercraft.api.network;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Fired on {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS} before an <b>automatic</b> connection between two
 * adjacent components is created (both {@link ConnectionRule}s already accepted it).
 * Cancel it to block the connection. Only fired on the server thread.
 */
public class ComponentConnectEvent extends Event implements ICancellableEvent
{
    private final ServerLevel level;
    private final NetworkComponent a;
    private final NetworkComponent b;
    private final Direction side;

    public ComponentConnectEvent(ServerLevel level, NetworkComponent a, NetworkComponent b, Direction side)
    {
        this.level = level;
        this.a = a;
        this.b = b;
        this.side = side;
    }

    public ServerLevel getLevel()
    {
        return level;
    }

    public NetworkComponent getA()
    {
        return a;
    }

    public NetworkComponent getB()
    {
        return b;
    }

    /// Side of {@link #getA()} that faces {@link #getB()}.
    public Direction getSide()
    {
        return side;
    }
}
