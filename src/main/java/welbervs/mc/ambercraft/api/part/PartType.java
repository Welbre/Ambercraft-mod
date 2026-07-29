package welbervs.mc.ambercraft.api.part;

import net.minecraft.resources.ResourceLocation;

public record PartType<T extends Part>(ResourceLocation id, T part)
{
}
