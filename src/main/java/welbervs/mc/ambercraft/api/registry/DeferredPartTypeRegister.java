package welbervs.mc.ambercraft.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.part.Part;
import welbervs.mc.ambercraft.api.part.PartType;

import java.util.function.Function;

public record DeferredPartTypeRegister(DeferredRegister<PartType<?>> register)
{
    public DeferredPartTypeRegister(String register)
    {
        this(DeferredRegister.create(AmbercraftRegistries.PART_TYPE, register));
    }

    /**
     * Register a new IPartType using your mod id.
     *
     * @param name     the name of the IPartType
     * @param partType The PartType to register
     * @param <T>      The class that will be registered
     * @return The DeferredHolder of the IPartType
     */
    public <T extends Part> DeferredHolder<PartType<?>, PartType<T>> register(String name, Function<ResourceLocation,T> partType)
    {
        var key = ResourceLocation.fromNamespaceAndPath(register.getNamespace(), name);
        return register.register(name, () -> new PartType<>(key, partType.apply(key)));
    }

    public void register(IEventBus modEventBus)
    {
        register.register(modEventBus);
    }
}
