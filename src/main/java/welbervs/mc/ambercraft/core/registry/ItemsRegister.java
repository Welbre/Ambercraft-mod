package welbervs.mc.ambercraft.core.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.api.registry.DataComponentRegister;
import welbervs.mc.ambercraft.content.items.ResistorPartItem;
import welbervs.mc.ambercraft.content.part_type.VoltageSourcePartType;

import java.util.function.Function;

import static welbervs.mc.ambercraft.core.Ambercraft.MODID;

public class ItemsRegister
{
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(MODID);

    public static final DeferredHolder<Item, ResistorPartItem> RESISTOR = REGISTER.registerItem("resistor", ResistorPartItem::new);
    public static final DeferredHolder<Item, Item> VOLTAGE_SOURCE = REGISTER.registerItem("voltage_source", new Function<Item.Properties, Item>()
    {
        @Override
        public Item apply(Item.Properties properties)
        {
            properties.component(
                    DataComponentRegister.PART_DATA_COMPONENT,
                    new PartDataComponent(PartTypesRegister.VOLTAGE_SOURCE_PART_TYPE)
            );
            var i = new Item(properties);
            return i;
        }
    }, new Item.Properties());
}
