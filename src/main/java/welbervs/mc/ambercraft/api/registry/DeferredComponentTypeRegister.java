package welbervs.mc.ambercraft.api.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.network.ConnectionRule;
import welbervs.mc.ambercraft.api.network.NetworkComponent;
import welbervs.mc.ambercraft.api.network.NetworkComponentType;
import welbervs.mc.ambercraft.api.network.NetworkDomain;

import java.util.function.Function;
import java.util.function.Supplier;

public record DeferredComponentTypeRegister(DeferredRegister<ComponentType<?>> register)
{
    public DeferredComponentTypeRegister(String register)
    {
        this(DeferredRegister.create(AmbercraftRegistries.COMPONENT_TYPE, register));
    }

    /**
     * Register a new componentType using your mod id.
     *
     * @param name the name of the ComponentType
     * @param componentType A function that receives the ResourceLocation of the ComponentType and returns the ComponentType
     * @param <T>  The class that will be registered
     * @return The DeferredHolder of the ComponentType
     */
    public <T extends ComponentType<?>> DeferredHolder<ComponentType<?>, T> register(String name, Function<ResourceLocation, T> componentType)
    {
        var key = ResourceLocation.fromNamespaceAndPath(register.getNamespace(), name);
        return register.register(name, () -> componentType.apply(key));
    }

    /**
     * Register a new componentType using your mod id.
     *
     * @param name              the name of the ComponentType
     * @param componentSupplier A simple supplier that returns a default component, we recommend using the ::new operator
     * @param codec             The codec of the component fields (without the "type" field)
     * @param <T>               The class that will be registered
     * @return                  The DeferredHolder of the ComponentType
     */
    public <T extends Component> DeferredHolder<ComponentType<?>, ComponentType<T>> simpleComponent(String name, Supplier<T> componentSupplier, MapCodec<T> codec)
    {
        return register(name, (id) -> new ComponentType<>(id, componentSupplier, codec));
    }

    /**
     * Register a {@link NetworkComponentType} of plain {@link NetworkComponent}s (no extra data).
     *
     * @param name   the name of the ComponentType
     * @param domain the domain of the components
     * @param rule   the connection rule, e.g. {@link ConnectionRule#SAME_DOMAIN}
     * @return The DeferredHolder of the ComponentType
     */
    public DeferredHolder<ComponentType<?>, NetworkComponentType<NetworkComponent>> simpleNetworkComponent(String name, NetworkDomain domain, ConnectionRule rule)
    {
        return register(name, (id) -> new NetworkComponentType<NetworkComponent>(id, domain, rule, NetworkComponent::new));
    }

    public void register(IEventBus modEventBus)
    {
        register.register(modEventBus);
    }
}
