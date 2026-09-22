package welbervs.mc.ambercraft.content.component_type;

import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.content.components.ElectricalResistenceComponent;

public class ElectricalResistenceComponentType extends ComponentType<Component>
{
    public ElectricalResistenceComponentType(ResourceLocation id)
    {
        super(id, ElectricalResistenceComponent::new);
    }
}
