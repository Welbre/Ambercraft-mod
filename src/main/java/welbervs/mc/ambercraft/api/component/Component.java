package welbervs.mc.ambercraft.api.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;


public interface Component extends INBTSerializable<CompoundTag>
{
    @NotNull ComponentType<?> getType();

    static CompoundTag SERIALIZE(Component component, HolderLookup.Provider provider)
    {
        CompoundTag tag = component.serializeNBT(provider);
        tag.putString("type", component.getType().id.toString());

        return tag;
    }

    static Component DESERIALIZE(HolderLookup.Provider provider, CompoundTag nbt)
    {
        ComponentType<?> type = AmbercraftRegistries.COMPONENT_TYPE_REGISTRY.get(ResourceLocation.parse(nbt.getString("type")));
        if (type == null)
            throw new IllegalStateException("Unknown component type: " + nbt.getString("type"));

        Component instance = type.getDefaultInstance();

        instance.deserializeNBT(provider, nbt);

        return instance;
    }
}
