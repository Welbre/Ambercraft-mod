package welbervs.mc.ambercraft.core.part;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.registry.BlocksRegister;
import welbervs.mc.ambercraft.api.registry.DataComponentRegister;

@EventBusSubscriber(modid = Ambercraft.MODID)
public class PartEvents
{
    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickBlock event)
    {
        PartDataComponent component = event.getItemStack().get(DataComponentRegister.PART_DATA_COMPONENT);
        if (component == null)
            return;

        var level = event.getLevel();

        BlockEntity be = level.getBlockEntity(event.getPos());
        //check if the block is a PartContainer, if it isn't, create it.'
        if (!(be instanceof PartContainerBlockEntity))
        {

            //face can't be null because the LeftClickBlock always returns a face.
            var relative = event.getPos().relative(event.getFace());

            //check if the block is already occupied
            if (!level.getBlockState(relative).isAir())//return if it isn't air, so can't be occupied
                return;

            level.setBlock(relative, BlocksRegister.PART_CONTAINER_BLOCK.get().defaultBlockState(), 3);
            be = level.getBlockEntity(relative);
        }

        //can't be null because the block is created above or already exists.
        PartContainerBlockEntity pCon = (PartContainerBlockEntity) be;
        if (! pCon.addPart(component.getDefaultPartInstance()))//try to add the part
            return;

        event.getItemStack().consume(1, event.getEntity());
    }
}
