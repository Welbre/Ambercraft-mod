package welbervs.mc.ambercraft.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.ipart.IPartType;
import welbervs.mc.ambercraft.api.ipart.IPart;

import java.util.function.Supplier;

public record DeferredIPartRegister(DeferredRegister<IPartType<?>> register)
{
    public DeferredIPartRegister(String register)
    {
        this(DeferredRegister.create(AmbercraftRegistries.IPART_TYPE, register));
    }

    /**
     * Register a new IPartType using your mod id.
     *
     * @param name     the name of the IPartType
     * @param defaults returns the default IPart of the IPartType, use to create a new IPart
     * @param <T>      The class that will be registered
     * @return The DeferredHolder of the IPartType
     */
    public <T extends IPart> DeferredHolder<IPartType<?>, IPartType<T>> register(String name, Supplier<T> defaults)
    {
        return register.register(name, () -> new IPartType<>(ResourceLocation.fromNamespaceAndPath(register.getNamespace(), name), defaults));
    }

    public void register(IEventBus modEventBus)
    {
        register.register(modEventBus);
    }
}
