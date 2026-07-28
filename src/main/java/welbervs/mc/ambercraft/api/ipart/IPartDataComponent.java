package welbervs.mc.ambercraft.api.ipart;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;

public record IPartDataComponent(Holder<IPartType<?>> partType)
{
    public @NotNull IPartType<?> getType()
    {
        return partType.value();
    }

    public <T extends IPart> @NotNull IPart getDefaultIPart()
    {
        return partType.value().getDefaultInstance();
    }

    public static final Codec<IPartDataComponent> DISK_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryFixedCodec.create(AmbercraftRegistries.IPART_TYPE)
                            .fieldOf("type")
                            .forGetter(IPartDataComponent::partType)
            ).apply(instance, IPartDataComponent::new)
    );

    public static final StreamCodec<ByteBuf,IPartDataComponent> NETWORK_CODEC = ByteBufCodecs.fromCodec(DISK_CODEC);
}
