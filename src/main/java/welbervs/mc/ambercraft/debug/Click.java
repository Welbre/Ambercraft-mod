package welbervs.mc.ambercraft.debug;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.api.registry.CapabilitiesRegister;

@EventBusSubscriber(modid = "ambercraft")
//todo deletar essa porcaria em !
public class Click
{
    @SubscribeEvent
    public static void on(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getPos() != null)
        {
            String url = "Sem capacidade";
            ComponentSourceCapability capability = event.getLevel().getCapability(CapabilitiesRegister.COMPONENT_SOURCE_CAPABILITY, event.getPos(), event.getFace());
            if (capability != null)
                url = "Com capacidade";
            event.getEntity().sendSystemMessage(
                Component.literal(url)
            );
        }
    }
}
