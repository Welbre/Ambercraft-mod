package welbervs.mc.ambercraft.api.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;


public interface Component
{
    /// Codec of any component, it writes a "type" field and delegates the rest to {@link ComponentType#codec()}.
    Codec<Component> CODEC = Codec.lazyInitialized(() ->
            AmbercraftRegistries.COMPONENT_TYPE_REGISTRY.byNameCodec()
                    .dispatch(Component::getType, ComponentType::codec));

    @NotNull ComponentType<?> getType();

    static CompoundTag SERIALIZE(Component component, HolderLookup.Provider provider)
    {
        Tag tag = CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), component)
                .getOrThrow(msg -> new IllegalStateException("Can't serialize component " + component.getType().id + ": " + msg));
        return (CompoundTag) tag;
    }

    static Component DESERIALIZE(HolderLookup.Provider provider, CompoundTag nbt)
    {
        return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt)
                .getOrThrow(msg -> new IllegalStateException("Can't deserialize component: " + msg));
    }
}
