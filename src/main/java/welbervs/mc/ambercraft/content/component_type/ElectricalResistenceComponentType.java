package welbervs.mc.ambercraft.content.component_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.content.components.ElectricalResistenceComponent;

import java.util.function.Supplier;

public class ElectricalResistenceComponentType extends ComponentType<ElectricalResistenceComponent>
{
    public ElectricalResistenceComponentType(ResourceLocation id)
    {
        super(id, ElectricalResistenceComponent::new, ElectricalResistenceComponent.CODEC);
    }
}
