package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.api.registry.DeferredPartTypeRegister;
import welbervs.mc.ambercraft.content.part_type.TestPartType;
import welbervs.mc.ambercraft.core.Ambercraft;

public class PartTypesRegister
{
    public static final DeferredPartTypeRegister REGISTER = new DeferredPartTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<PartType, TestPartType> TEST_PART_TYPE = REGISTER.register("test_part_type", TestPartType::new);
}
