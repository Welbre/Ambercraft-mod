package welbervs.mc.ambercraft.content.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class HeatDissipatorComponent implements Component
{
    private double resistance;

    public HeatDissipatorComponent()
    {
    }

    public HeatDissipatorComponent(double resistance)
    {
        this.resistance = resistance;
    }

    @Override
    public @NotNull ComponentType<?> getType()
    {
        return ComponentTypeRegister.HEAT_DISSIPATOR.get();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("r", resistance);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        resistance = nbt.getInt("r");
    }


    public double getResistance()
    {
        return resistance;
    }

    public void setResistance(double resistence)
    {
        this.resistance = resistence;
    }
}
