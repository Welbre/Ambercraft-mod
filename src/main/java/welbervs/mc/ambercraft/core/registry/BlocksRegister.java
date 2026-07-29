package welbervs.mc.ambercraft.core.registry;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.core.part.PartContainerBlock;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class BlocksRegister
{
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(MODID);

    public static final DeferredHolder<Block, PartContainerBlock> PART_CONTAINER_BLOCK = REGISTER.registerBlock("part_container", PartContainerBlock::new);
}
