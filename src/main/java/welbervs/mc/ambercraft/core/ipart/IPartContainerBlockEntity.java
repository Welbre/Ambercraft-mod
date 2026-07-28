package welbervs.mc.ambercraft.core.ipart;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.ipart.IPart;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IPartContainerBlockEntity extends BlockEntity implements Iterable<IPart>
{
    private final ArrayList<IPart> parts = new ArrayList<>();

    public IPartContainerBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockEntityRegister.IPART_CONTAINER_BLOCK_ENTITY.get(), pos, blockState);
    }

    /**
     * Try to add an IPart to the container.
     *
     * @param iPart The IPart to add
     * @return true if successful.
     */
    public boolean addPart(IPart iPart)
    {
        parts.add(iPart);
        return true;
    }

    @Override
    public @NotNull Iterator<IPart> iterator()
    {
        return parts.iterator();
    }
}
