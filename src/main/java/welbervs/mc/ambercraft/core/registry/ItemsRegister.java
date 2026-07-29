package welbervs.mc.ambercraft.core.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.content.items.TestPartItem;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class ItemsRegister
{
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MODID);

    public static final DeferredHolder<Item, TestPartItem> TEST_PART_ITEM = REGISTER.registerItem("test_part", TestPartItem::new);
}
