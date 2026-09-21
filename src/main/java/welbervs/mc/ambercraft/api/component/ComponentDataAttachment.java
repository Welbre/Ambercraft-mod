package welbervs.mc.ambercraft.api.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

/// Used to store component the data in BlockEntity using the attachment system.
public class ComponentDataAttachment implements INBTSerializable<CompoundTag>
{
    public ComponentDataAttachment()
    {
    }


    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider)
    {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt)
    {

    }
}
