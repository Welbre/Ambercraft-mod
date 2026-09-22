package welbervs.mc.ambercraft.content.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class ElectricalSourceComponent implements Component
{
    private double voltage;

    public ElectricalSourceComponent()
    {
    }
    public ElectricalSourceComponent(double voltage)
    {
        this.voltage = voltage;
    }

    @Override
    public @NotNull ComponentType<?> getType()
    {
        return ComponentTypeRegister.ELECTRICAL_SOURCE.get();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("v", voltage);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        voltage = nbt.getDouble("v");
    }

    public double getVoltage()
    {
        return voltage;
    }

    public void setVoltage(double voltage)
    {
        this.voltage = voltage;
    }
}
