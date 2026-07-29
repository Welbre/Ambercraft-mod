package welbervs.mc.ambercraft.core.part;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.part.PartInstance;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

import java.util.ArrayList;
import java.util.Iterator;

public class PartContainerBlockEntity extends BlockEntity implements Iterable<PartInstance>
{
    private final ArrayList<PartInstance> partInstances = new ArrayList<>();

    public PartContainerBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockEntityRegister.PART_CONTAINER_BLOCK_ENTITY.get(), pos, blockState);
    }

    /**
     * Try to add an IPart to the container.
     *
     * @param partInstance The IPart to add
     * @return true if successful.
     */
    public boolean addPart(PartInstance partInstance)
    {
        partInstances.add(partInstance);
        return true;
    }

    @Override
    public @NotNull Iterator<PartInstance> iterator()
    {
        return partInstances.iterator();
    }
}
