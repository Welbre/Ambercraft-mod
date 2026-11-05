package welbervs.mc.ambercraft.content.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class ElectricalSourceComponent implements Component
{
    public static final MapCodec<ElectricalSourceComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("v", 0.0).forGetter(ElectricalSourceComponent::getVoltage)
    ).apply(instance, ElectricalSourceComponent::new));

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

    public double getVoltage()
    {
        return voltage;
    }

    public void setVoltage(double voltage)
    {
        this.voltage = voltage;
    }
}
