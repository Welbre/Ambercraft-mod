package welbervs.mc.ambercraft.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.ipart.IPartType;
import welbervs.mc.ambercraft.core.Ambercraft;

/**
 * Contains all the ambercraft registry used for API's
 */
public class AmbercraftRegistries
{
    public static final ResourceKey<Registry<IPartType<?>>> IPART_TYPE =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(Ambercraft.MODID, "ipart_type")
            );
}
