package welbervs.mc.ambercraft.content.component_type;

import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.content.components.TestComponent;

public class TestComponentType extends ComponentType<Component>
{
    public TestComponentType(ResourceLocation id)
    {
        super(id, () -> new TestComponent());
    }
}
