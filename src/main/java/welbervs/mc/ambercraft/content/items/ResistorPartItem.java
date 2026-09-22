package welbervs.mc.ambercraft.content.items;

import net.minecraft.world.item.Item;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.api.registry.DataComponentRegister;
import welbervs.mc.ambercraft.core.registry.PartTypesRegister;

public class ResistorPartItem extends Item
{
    public ResistorPartItem(Properties properties)
    {
        super(properties.component(DataComponentRegister.PART_DATA_COMPONENT, new PartDataComponent(PartTypesRegister.RESISTOR_PART_TYPE)));
    }
}
