package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.registry.DeferredComponentTypeRegister;
import welbervs.mc.ambercraft.content.component_type.ElectricalResistenceComponentType;
import welbervs.mc.ambercraft.content.component_type.HeatDissipatorComponentType;
import welbervs.mc.ambercraft.content.components.ElectricalSourceComponent;
import welbervs.mc.ambercraft.core.Ambercraft;

///Register all component used in the AmberCraft mod
public class ComponentTypeRegister
{
    public static final DeferredComponentTypeRegister REGISTER = new DeferredComponentTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<ComponentType<?>, ElectricalResistenceComponentType> ELECTRICAL_RESISTENCE = REGISTER.register("electrical_resistence", ElectricalResistenceComponentType::new);
    public static final DeferredHolder<ComponentType<?>, ComponentType<ElectricalSourceComponent>> ELECTRICAL_SOURCE = REGISTER.simpleComponent("electrical_source", ElectricalSourceComponent::new);
    public static final DeferredHolder<ComponentType<?>, HeatDissipatorComponentType> HEAT_DISSIPATOR = REGISTER.register("heat_dissipator", HeatDissipatorComponentType::new);
}
