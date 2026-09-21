package welbervs.mc.ambercraft.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.core.Ambercraft;

/**
 * Contains all the ambercraft registry used for API's
 */
public class AmbercraftRegistries
{
    public static final ResourceKey<Registry<PartType>> PART_TYPE =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(Ambercraft.MODID, "part_type")
            );

    public static final ResourceKey<Registry<ComponentType<?>>> COMPONENT_TYPE =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(Ambercraft.MODID, "component_type")
            );

    public static final Registry<PartType> PART_TYPE_REGISTRY =
            new RegistryBuilder<>(PART_TYPE)
                    .sync(true)
                    .create();

    public static final Registry<ComponentType<?>> COMPONENT_TYPE_REGISTRY =
            new RegistryBuilder<>(COMPONENT_TYPE)
                    .sync(true)
                    .create();

    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(PART_TYPE_REGISTRY);
        event.register(COMPONENT_TYPE_REGISTRY);
    }
}
