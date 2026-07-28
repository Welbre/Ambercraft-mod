package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.registry.DeferredIPartRegister;
import welbervs.mc.ambercraft.api.ipart.IPartType;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.content.iparts.TestIPart;

public class IPartsRegister
{
    public static final DeferredIPartRegister REGISTER = new DeferredIPartRegister(Ambercraft.MODID);

    public static final DeferredHolder<IPartType<?>, IPartType<TestIPart>> TEST_IPART = REGISTER.register("test_ipart", () -> new TestIPart(0x0000FF));


}
