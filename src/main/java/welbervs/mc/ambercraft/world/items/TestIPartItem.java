package welbervs.mc.ambercraft.world.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import welbervs.mc.ambercraft.api.ipart.IPartDataComponent;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;
import welbervs.mc.ambercraft.core.registry.IPartsRegister;

public class TestIPartItem extends Item
{
    public static final CompoundTag TEST;
    static {
        TEST = new CompoundTag();
        TEST.putString("juse", "helena");
    }

    public TestIPartItem(Properties properties)
    {
        super(properties.component(DataComponentRegister.IPART_DATA_COMPONENT, new IPartDataComponent(IPartsRegister.TEST_IPART.getId(), TEST)));
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        IPartDataComponent component = context.getItemInHand().get(DataComponentRegister.IPART_DATA_COMPONENT);
        if (component != null)
            context.getPlayer().sendSystemMessage(Component.literal(
                    component.data().getString("juse")
            ));
        return super.useOn(context);
    }
}
