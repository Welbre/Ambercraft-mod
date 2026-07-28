package welbervs.mc.ambercraft.api.ipart;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record IPartDataComponent(ResourceLocation ID, CompoundTag data)
{
    public static final Codec<IPartDataComponent> DISK_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("ID").forGetter(IPartDataComponent::ID),
                    CompoundTag.CODEC.fieldOf("data").forGetter(IPartDataComponent::data)
            ).apply(instance, IPartDataComponent::new)
    );

    public static final StreamCodec<ByteBuf,IPartDataComponent> NETWORK_CODEC = ByteBufCodecs.fromCodec(DISK_CODEC);
}
