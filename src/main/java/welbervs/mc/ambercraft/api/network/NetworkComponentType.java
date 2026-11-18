package welbervs.mc.ambercraft.api.network;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The behaviour of a {@link NetworkComponent}: its {@link NetworkDomain}, the {@link ConnectionRule} and the callbacks
 * fired when the graph changes. Override the methods for custom behaviour.
 * <p>
 * All the callbacks run on the server thread, while the {@link NetworkManager} is changing the graph. Don't change the
 * graph inside them, use {@link NetworkManager#markDirty(net.minecraft.core.BlockPos)} if you need a refresh.
 */
public class NetworkComponentType<T extends NetworkComponent> extends ComponentType<T>
{
    private final NetworkDomain domain;
    private final ConnectionRule connectionRule;

    /**
     * Creates a type for components <b>without persistent data</b>.
     *
     * @param factory Creates a new component, receives this type. For the base class use {@code NetworkComponent::new}.
     */
    public NetworkComponentType(@NotNull ResourceLocation id, @NotNull NetworkDomain domain, @NotNull ConnectionRule connectionRule,
                                @NotNull Function<NetworkComponentType<T>, T> factory)
    {
        this(id, domain, connectionRule, factory, new AtomicReference<>());
    }

    // The self reference is only read lazily (when a component is created), so it's safe to set it after super().
    private NetworkComponentType(ResourceLocation id, NetworkDomain domain, ConnectionRule connectionRule,
                                 Function<NetworkComponentType<T>, T> factory, AtomicReference<NetworkComponentType<T>> self)
    {
        super(id, () -> factory.apply(self.get()), MapCodec.unit(() -> factory.apply(self.get())));
        self.set(this);
        this.domain = domain;
        this.connectionRule = connectionRule;
    }

    /**
     * Creates a type for components with persistent data.
     *
     * @param defaultInstance Creates a component with default values.
     * @param codec           The codec of the component's own fields (without the "type" field).
     */
    public NetworkComponentType(@NotNull ResourceLocation id, @NotNull NetworkDomain domain, @NotNull ConnectionRule connectionRule,
                                @NotNull Supplier<T> defaultInstance, @NotNull MapCodec<T> codec)
    {
        super(id, defaultInstance, codec);
        this.domain = domain;
        this.connectionRule = connectionRule;
    }

    public NetworkDomain getDomain()
    {
        return domain;
    }

    public ConnectionRule getConnectionRule()
    {
        return connectionRule;
    }

    /**
     * Whether {@code self} (a component of this type) accepts a connection with {@code other}. The connection is only
     * created when both sides accept it.
     *
     * @param side The side of {@code self} where the connection happens, null for manual connections.
     */
    public boolean canConnect(NetworkComponent self, NetworkComponent other, @Nullable Direction side)
    {
        return connectionRule.test(self, other, side);
    }

    /// Called after a connection with {@code self} is created.
    public void onConnected(NetworkComponent self, NetworkConnection connection)
    {
    }

    /// Called after a connection with {@code self} is removed.
    public void onDisconnected(NetworkComponent self, NetworkConnection connection)
    {
    }

    /**
     * Called when {@code self} changes from one network to another.
     * {@code oldNetwork} is null when the component was just registered and {@code newNetwork} is null when it was removed.
     */
    public void onNetworkChanged(NetworkComponent self, @Nullable Network oldNetwork, @Nullable Network newNetwork)
    {
    }
}
