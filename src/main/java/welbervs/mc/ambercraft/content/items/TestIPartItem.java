package welbervs.mc.ambercraft.content.items;

import net.minecraft.world.item.Item;
import welbervs.mc.ambercraft.api.ipart.IPartDataComponent;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;
import welbervs.mc.ambercraft.core.registry.IPartsRegister;

public class TestIPartItem extends Item
{
    public TestIPartItem(Properties properties)
    {
        super(properties.component(DataComponentRegister.IPART_DATA_COMPONENT, new IPartDataComponent(IPartsRegister.TEST_IPART)));
    }
}
