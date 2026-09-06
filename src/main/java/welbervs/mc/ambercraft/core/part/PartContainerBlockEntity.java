package welbervs.mc.ambercraft.core.part;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.part.Part;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

import java.util.ArrayList;
import java.util.Iterator;

public class PartContainerBlockEntity extends BlockEntity implements Iterable<Part>
{
    private final ArrayList<Part> parts = new ArrayList<>();

    public PartContainerBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockEntityRegister.PART_CONTAINER_BLOCK_ENTITY.get(), pos, blockState);
    }

    /**
     * Try to add an IPart to the container.
     *
     * @param part The IPart to add
     * @return true if successful.
     */
    public boolean addPart(Part part)
    {
        parts.add(part);
        return true;
    }

    @Override
    public @NotNull Iterator<Part> iterator()
    {
        return parts.iterator();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------Data handler------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries)
    {
        super.saveAdditional(tag, registries);
        CompoundTag pInstances = new CompoundTag();
        for (int i = 0; i < parts.size(); i++)
        {
            Part instance = parts.get(i);
            CompoundTag _tag = new CompoundTag();
            try {
                instance.saveAdditional(_tag);
            } catch (RuntimeException e)
            {
                Ambercraft.LOGGER.error("Serialization fail", e);
            }
            pInstances.put(String.valueOf(i), _tag);
        }
        tag.put("partInstance", pInstances);
    }
}
