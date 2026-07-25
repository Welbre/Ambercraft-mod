package welbervs.mc.ambercraft.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class CreativeTabRegister
{
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AMBERCRAFT_TAB = REGISTER.register("ambercraft_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ambercraft.main")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.DIAMOND::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.accept(ItemsRegister.TEST_IPART_ITEM.get().getDefaultInstance());
                //output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());
}
