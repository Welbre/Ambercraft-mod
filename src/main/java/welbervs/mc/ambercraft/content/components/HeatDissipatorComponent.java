package welbervs.mc.ambercraft.content.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class HeatDissipatorComponent implements Component
{
    public static final MapCodec<HeatDissipatorComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("r", 0.0).forGetter(HeatDissipatorComponent::getResistance)
    ).apply(instance, HeatDissipatorComponent::new));

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

    public double getResistance()
    {
        return resistance;
    }

    public void setResistance(double resistance)
    {
        this.resistance = resistance;
    }
}
