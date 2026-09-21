package welbervs.mc.ambercraft.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.part.PartType;

import java.util.function.Function;

public record DeferredPartTypeRegister(DeferredRegister<PartType> register)
{
    public DeferredPartTypeRegister(String register)
    {
        this(DeferredRegister.create(AmbercraftRegistries.PART_TYPE, register));
    }

    /**
     * Register a new IPartType using your mod id.
     *
     * @param name the name of the PartType
     * @param partType A function that receives the ResourceLocation of the PartType and returns the PartType
     * @param <T>  The class that will be registered
     * @return The DeferredHolder of the PartType
     */
    public <T extends PartType> DeferredHolder<PartType, T> register(String name, Function<ResourceLocation, T> partType)
    {
        var key = ResourceLocation.fromNamespaceAndPath(register.getNamespace(), name);
        return register.register(name, () -> partType.apply(key));
    }

    public void register(IEventBus modEventBus)
    {
        register.register(modEventBus);
    }
}
