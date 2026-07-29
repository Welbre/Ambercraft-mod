package welbervs.mc.ambercraft.core.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.part.PartContainerBlockEntity;
import welbervs.mc.ambercraft.core.registry.BlocksRegister;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;

@EventBusSubscriber(modid = Ambercraft.MODID)
public class PartEvents
{
    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickBlock event)
    {
        PartDataComponent component = event.getItemStack().get(DataComponentRegister.PART_DATA_COMPONENT);
        if (component == null)
            return;

        //face can't be null because the LeftClickBlock always returns a face.
        var relative = event.getPos().relative(event.getFace());
        var state = event.getLevel().getBlockState(relative);
        boolean isAir = state.isAir();

        //check if the block is already occupied
        if (!isAir && !state.is(BlocksRegister.PART_CONTAINER_BLOCK.get()))
            return;

        var level = event.getLevel();

        if (isAir)
        {
            level.setBlock(relative, BlocksRegister.PART_CONTAINER_BLOCK.get().defaultBlockState(), 3);
        }

        //can't be null because the block is created above or already exists.
        PartContainerBlockEntity be = (PartContainerBlockEntity) level.getBlockEntity(relative);
        if (! be.addPart(component.getDefaultPartInstance()))//try to add the part
            return;

        event.getItemStack().consume(1, event.getEntity());
    }
}
