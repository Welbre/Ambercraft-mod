package welbervs.mc.ambercraft.content.components;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class ElectricalResistenceComponent implements Component
{
    private double resistance;

    public ElectricalResistenceComponent()
    {
    }

    public ElectricalResistenceComponent(double resistance)
    {
        this.resistance = resistance;
    }

    @Override
    public @NotNull ComponentType<?> getType()
    {
        return ComponentTypeRegister.ELECTRICAL_RESISTENCE.get();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider)
    {
        var x = new CompoundTag();
        x.putDouble("r", resistance);
        return x;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt)
    {
        resistance = nbt.getDouble("r");
    }

    public double getResistance()
    {
        return resistance;
    }

    public void setResistance(double resistance)
    {
        this.resistance = resistance;
    }
}
