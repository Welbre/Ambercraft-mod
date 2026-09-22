package welbervs.mc.ambercraft.content.component_type;

import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.content.components.HeatDissipatorComponent;

public class HeatDissipatorComponentType extends ComponentType<HeatDissipatorComponent>
{
    public HeatDissipatorComponentType(ResourceLocation id)
    {
        super(id, HeatDissipatorComponent::new);
    }
}
