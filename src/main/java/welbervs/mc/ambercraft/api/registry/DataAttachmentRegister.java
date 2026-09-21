package welbervs.mc.ambercraft.api.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import welbervs.mc.ambercraft.api.component.ComponentDataAttachment;
import welbervs.mc.ambercraft.core.Ambercraft;

import java.util.function.Supplier;

public class DataAttachmentRegister
{
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ambercraft.MODID);

    public static final Supplier<AttachmentType<ComponentDataAttachment>> COMPONENT_DATA_ATTACHMENT = ATTACHMENT_TYPES.register(
            "component_data_attachment", () -> AttachmentType.serializable(ComponentDataAttachment::new).build()
    );
}
