package welbervs.mc.ambercraft.content.items;

import net.minecraft.world.item.Item;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.api.registry.DataComponentRegister;
import welbervs.mc.ambercraft.core.registry.PartTypesRegister;

public class TestPartItem extends Item
{
    public TestPartItem(Properties properties)
    {
        super(properties.component(DataComponentRegister.PART_DATA_COMPONENT, new PartDataComponent(PartTypesRegister.TEST_PART_TYPE)));
    }
}
