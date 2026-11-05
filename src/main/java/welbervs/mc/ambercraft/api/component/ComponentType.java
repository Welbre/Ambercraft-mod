package welbervs.mc.ambercraft.api.component;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Describes a kind of {@link Component}: how to create a default instance and how to (de)serialize it.
 * The {@link #codec()} is the single source of truth for disk, network and data-driven (json) formats.
 */
public class ComponentType<T extends Component>
{
    public final ResourceLocation id;
    private final Supplier<T> defaultInstance;
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    /**
     * @param id              The ResourceLocation where the ComponentType will be registered.
     * @param defaultInstance Creates a component with default values.
     * @param codec           The codec used to read/write the component's own fields. The "type" field is handled by {@link Component#CODEC}.
     */
    public ComponentType(ResourceLocation id, Supplier<T> defaultInstance, MapCodec<T> codec)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
        this.codec = codec;
        this.streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());
    }

    protected ComponentType(ResourceLocation id, MapCodec<T> codec)
    {
        this(id, () -> null, codec);
    }

    public T getDefaultInstance()
    {
        return defaultInstance.get();
    }

    public MapCodec<T> codec()
    {
        return codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec()
    {
        return streamCodec;
    }
}
