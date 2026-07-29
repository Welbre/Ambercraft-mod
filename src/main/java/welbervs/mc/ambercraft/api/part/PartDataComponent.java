package welbervs.mc.ambercraft.api.part;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;

public record PartDataComponent(Holder<PartType<?>> partType)
{
    public @NotNull PartInstance getDefaultPartInstance()
    {
        return partType.value().part().getDefaultInstance();
    }

    public static final Codec<PartDataComponent> DISK_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryFixedCodec.create(AmbercraftRegistries.PART_TYPE)
                            .fieldOf("type")
                            .forGetter(PartDataComponent::partType)
            ).apply(instance, PartDataComponent::new)
    );

    public static final StreamCodec<ByteBuf, PartDataComponent> NETWORK_CODEC = ByteBufCodecs.fromCodec(DISK_CODEC);
}
