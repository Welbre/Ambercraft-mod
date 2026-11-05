package welbervs.mc.ambercraft.content.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class ElectricalResistenceComponent implements Component
{
    public static final MapCodec<ElectricalResistenceComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("r", 0.0).forGetter(ElectricalResistenceComponent::getResistance)
    ).apply(instance, ElectricalResistenceComponent::new));

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

    public double getResistance()
    {
        return resistance;
    }

    public void setResistance(double resistance)
    {
        this.resistance = resistance;
    }
}
