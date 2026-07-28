package welbervs.mc.ambercraft.api.ipart;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class IPartType<T extends IPart>
{
    public final ResourceLocation id;
    private final Supplier<T> defaultInstance;

    /**
     * Creates a new IPartType
     *
     * @param id              The resourceLocation where the IPartType will be registered
     * @param defaultInstance A supplier that returns the default IPart.
     */
    public IPartType(ResourceLocation id, Supplier<T> defaultInstance)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
    }

    public T getDefaultInstance()
    {
        return defaultInstance.get();
    }
}
