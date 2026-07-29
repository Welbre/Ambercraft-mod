package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.api.registry.DeferredPartTypeRegister;
import welbervs.mc.ambercraft.content.part_type.TestPart;
import welbervs.mc.ambercraft.core.Ambercraft;

public class PartsRegister
{
    public static final DeferredPartTypeRegister REGISTER = new DeferredPartTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<PartType<?>, PartType<TestPart>> TEST_PART = REGISTER.register("test_part", TestPart::new);

}
