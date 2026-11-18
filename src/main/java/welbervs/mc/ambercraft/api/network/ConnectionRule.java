package welbervs.mc.ambercraft.api.network;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Decides if a {@link NetworkComponent} accepts a connection with another one.
 * <p>
 * A connection is only created when <b>both</b> sides accept it, i.e.
 * {@code a.type.canConnect(a, b, side) && b.type.canConnect(b, a, side.getOpposite())}.
 * Rules can be combined with {@link #and(ConnectionRule)}, {@link #or(ConnectionRule)} and {@link #negate()}.
 */
@FunctionalInterface
public interface ConnectionRule
{
    /**
     * @param self  The component that owns the rule.
     * @param other The component that wants to connect with {@code self}.
     * @param side  The side of {@code self} where the connection happens, or {@code null} for non-directional (manual) connections.
     * @return true if {@code self} accepts the connection.
     */
    boolean test(NetworkComponent self, NetworkComponent other, @Nullable Direction side);

    ConnectionRule ALWAYS = (self, other, side) -> true;
    ConnectionRule NEVER = (self, other, side) -> false;

    /// Only connects with components of the same {@link NetworkDomain}.
    ConnectionRule SAME_DOMAIN = (self, other, side) -> self.getDomain() == other.getDomain();

    /// Only connects with components of the exact same {@link NetworkComponentType}.
    ConnectionRule SAME_TYPE = (self, other, side) -> self.getType() == other.getType();

    /// Only connects with components whose domain is one of the given ones.
    static ConnectionRule domainIn(NetworkDomain... domains)
    {
        List<NetworkDomain> list = List.of(domains);
        return (self, other, side) -> list.contains(other.getDomain());
    }

    /// Only connects through the given sides. Non-directional connections ({@code side == null}) are refused.
    static ConnectionRule onlySides(Direction... sides)
    {
        Set<Direction> set = sides.length == 0 ? EnumSet.noneOf(Direction.class) : EnumSet.of(sides[0], sides);
        return (self, other, side) -> side != null && set.contains(side);
    }

    default ConnectionRule and(ConnectionRule other)
    {
        return (self, o, side) -> this.test(self, o, side) && other.test(self, o, side);
    }

    default ConnectionRule or(ConnectionRule other)
    {
        return (self, o, side) -> this.test(self, o, side) || other.test(self, o, side);
    }

    default ConnectionRule negate()
    {
        return (self, o, side) -> !this.test(self, o, side);
    }
}
