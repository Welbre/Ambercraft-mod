package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.network.ConnectionRule;
import welbervs.mc.ambercraft.api.network.NetworkComponent;
import welbervs.mc.ambercraft.api.network.NetworkComponentType;
import welbervs.mc.ambercraft.api.network.NetworkDomain;
import welbervs.mc.ambercraft.api.registry.DeferredComponentTypeRegister;
import welbervs.mc.ambercraft.content.component_type.ElectricalResistenceComponentType;
import welbervs.mc.ambercraft.content.component_type.HeatDissipatorComponentType;
import welbervs.mc.ambercraft.content.components.ElectricalSourceComponent;
import welbervs.mc.ambercraft.core.Ambercraft;

///Register all component used in the AmberCraft mod
public class ComponentTypeRegister
{
    /// todo Preciso finalizar o sistema de component, fazer com que blocos com componentes de network criem as conexões de forma correta, definir as regras para criação de network etc...
    /// todo deixar para terminar o part depois, fazer os teste iniciais com blocos criativos simples, fontes de tensão de bloco cheio.
    public static final DeferredComponentTypeRegister REGISTER = new DeferredComponentTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<ComponentType<?>, ElectricalResistenceComponentType> ELECTRICAL_RESISTENCE = REGISTER.register("electrical_resistence", ElectricalResistenceComponentType::new);
    public static final DeferredHolder<ComponentType<?>, ComponentType<ElectricalSourceComponent>> ELECTRICAL_SOURCE = REGISTER.simpleComponent("electrical_source", ElectricalSourceComponent::new, ElectricalSourceComponent.CODEC);
    public static final DeferredHolder<ComponentType<?>, HeatDissipatorComponentType> HEAT_DISSIPATOR = REGISTER.register("heat_dissipator", HeatDissipatorComponentType::new);

    // Network components used by the NetworkTestBlock
    public static final DeferredHolder<ComponentType<?>, NetworkComponentType<NetworkComponent>> NETWORK_TEST_ELECTRICAL = REGISTER.simpleNetworkComponent("network_test_electrical", NetworkDomain.ELECTRICAL, ConnectionRule.SAME_DOMAIN);
    public static final DeferredHolder<ComponentType<?>, NetworkComponentType<NetworkComponent>> NETWORK_TEST_THERMAL = REGISTER.simpleNetworkComponent("network_test_thermal", NetworkDomain.THERMAL, ConnectionRule.SAME_DOMAIN);
}
