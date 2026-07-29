package welbervs.mc.ambercraft.content.items;

import net.minecraft.world.item.Item;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;
import welbervs.mc.ambercraft.core.registry.PartsRegister;

public class TestPartItem extends Item
{
    public TestPartItem(Properties properties)
    {
        super(properties.component(DataComponentRegister.PART_DATA_COMPONENT, new PartDataComponent(PartsRegister.TEST_PART)));
    }
}
