package welbervs.mc.ambercraft.world.items;

import net.minecraft.world.item.Item;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;
import welbervs.mc.ambercraft.ipart.IPartData;

public class TestIPartItem extends Item
{
    public TestIPartItem(Properties properties)
    {
        super(properties);
        properties.component(DataComponentRegister.IPART_DATA_COMPONENT, new IPartData.IPartDataComponent(20));
    }
}
