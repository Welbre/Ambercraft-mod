package welbervs.mc.ambercraft.ipart;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface IPartData
{


    record IPartDataComponent(int color)
    {
        public static final Codec<IPartDataComponent> DISK_CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("color").forGetter(IPartDataComponent::color)
                ).apply(instance, IPartDataComponent::new)
        );

        public static final StreamCodec<ByteBuf,IPartDataComponent> NETWORK_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, IPartDataComponent::color,
                IPartDataComponent::new
        );
    }
}
