package welbervs.mc.ambercraft.content.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import welbervs.mc.ambercraft.api.component.Component;

public class TestComponent implements Component
{
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        var x = new CompoundTag();
        x.putInt("x", 1024);
        return x;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {

    }
}
