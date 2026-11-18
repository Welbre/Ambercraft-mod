package welbervs.mc.ambercraft.api.registry;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import welbervs.mc.ambercraft.api.component.ComponentDataAttachment;
import welbervs.mc.ambercraft.core.Ambercraft;

import java.util.Arrays;
import java.util.function.Supplier;

public class DataAttachmentRegister
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ambercraft.MODID);

    private static final Codec<LongOpenHashSet> LONG_SET_CODEC = Codec.LONG_STREAM.xmap(
            stream -> new LongOpenHashSet(stream.toArray()),
            set -> Arrays.stream(set.toLongArray())
    );

    public static final Supplier<AttachmentType<ComponentDataAttachment>> COMPONENT_DATA_ATTACHMENT = ATTACHMENT_TYPES.register(
            "component_data_attachment", () -> AttachmentType.serializable(ComponentDataAttachment::new).build()
    );

    /// Stored in chunks: the positions (BlockPos#asLong) that have network components, refreshed when the chunk loads.
    public static final Supplier<AttachmentType<LongOpenHashSet>> NETWORK_POSITIONS = ATTACHMENT_TYPES.register(
            "network_positions", () -> AttachmentType.builder(() -> new LongOpenHashSet())
                    .serialize(LONG_SET_CODEC, set -> !set.isEmpty())
                    .build()
    );
}
