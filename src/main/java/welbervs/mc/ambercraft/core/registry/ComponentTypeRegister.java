package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.registry.DeferredComponentTypeRegister;
import welbervs.mc.ambercraft.content.component_type.TestComponentType;
import welbervs.mc.ambercraft.core.Ambercraft;

///Register all component used in the AmberCraft mod
public class ComponentTypeRegister
{
    public static final DeferredComponentTypeRegister REGISTER = new DeferredComponentTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<ComponentType<?>, TestComponentType> TEST_COMPONENT_TYPE = REGISTER.register("test_component_type", TestComponentType::new);
}
