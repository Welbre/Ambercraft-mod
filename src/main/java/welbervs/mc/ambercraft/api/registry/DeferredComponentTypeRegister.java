package welbervs.mc.ambercraft.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;

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
     * @param componentSupplier A simple supplier that returns the ComponentType desired to be registered, we recommend using the ::new operator
     * @param <T>               The class that will be registered
     * @return                  The DeferredHolder of the ComponentType
     */
    public <T extends Component> DeferredHolder<ComponentType<?>, ComponentType<T>> simpleComponent(String name, Supplier<T> componentSupplier)
    {
        return register(name, (id) -> new ComponentType<T>(id, componentSupplier));
    }

    public void register(IEventBus modEventBus)
    {
        register.register(modEventBus);
    }
}
