package welbervs.mc.ambercraft.api.registry;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.part.PartContainerBlockEntity;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

@EventBusSubscriber(modid = Ambercraft.MODID)
public class CapabilitiesRegister
{
    public static final BlockCapability<ComponentSourceCapability, ComponentSourceCapability.@NotNull Context> COMPONENT_SOURCE_CAPABILITY =
            BlockCapability.create(
                    ResourceLocation.fromNamespaceAndPath(Ambercraft.MODID, "component_source"),
                    ComponentSourceCapability.class,
                    ComponentSourceCapability.Context.class);


    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(
                COMPONENT_SOURCE_CAPABILITY,
                BlockEntityRegister.PART_CONTAINER_BLOCK_ENTITY.get(),
                PartContainerBlockEntity::formulateCapability
        );
        event.registerBlock(
                COMPONENT_SOURCE_CAPABILITY,
                (level, pos, state, blockEntity, context) ->
                {
                    if (state.is(Blocks.ICE) && context.direction() == Direction.UP)
                        return new ComponentSourceCapability();
                    if (state.is(Blocks.FURNACE))
                        return new ComponentSourceCapability();
                    return null;
                },
                Blocks.ICE, Blocks.FURNACE
        );
    }
}
