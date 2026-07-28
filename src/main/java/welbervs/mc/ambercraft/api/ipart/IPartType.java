package welbervs.mc.ambercraft.api.ipart;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class IPartType<T extends IPart>
{
    ResourceLocation id;
    Supplier<T> factory;

    public IPartType(ResourceLocation id, Supplier<T> factory)
    {
        this.id = id;
        this.factory = factory;
    }
}
