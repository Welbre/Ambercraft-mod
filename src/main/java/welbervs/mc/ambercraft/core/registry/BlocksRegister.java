package welbervs.mc.ambercraft.core.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import welbervs.mc.ambercraft.content.blocks.NetworkTestBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.core.part.PartContainerBlock;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class BlocksRegister
{
    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(MODID);

    public static final DeferredHolder<Block, PartContainerBlock> PART_CONTAINER_BLOCK = REGISTER.registerBlock("part_container", PartContainerBlock::new);

    public static final DeferredHolder<Block, NetworkTestBlock> NETWORK_TEST_BLOCK = REGISTER.registerBlock("network_test_block", NetworkTestBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5f).sound(SoundType.COPPER));
}
