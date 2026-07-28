package welbervs.mc.ambercraft.core.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.content.items.TestIPartItem;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class ItemsRegister
{
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MODID);

    public static final DeferredHolder<Item, TestIPartItem> TEST_IPART_ITEM = REGISTER.registerItem("test_ipart", TestIPartItem::new);
}
