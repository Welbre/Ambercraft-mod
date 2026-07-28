package welbervs.mc.ambercraft.core.ipart;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

public class IPartContainerBlockEntity extends BlockEntity
{
    public IPartContainerBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockEntityRegister.IPART_CONTAINER_BLOCK_ENTITY.get(), pos, blockState);
    }
}
