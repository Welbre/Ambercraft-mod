package welbervs.mc.ambercraft.api.part;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;

import java.util.Arrays;
import java.util.List;

public final class Part implements INBTSerializable<CompoundTag>
{
    private Component[] components;
    private PartType partType;

    public Part()
    {
    }

    public Part(PartType partType)
    {
        this.partType = partType;
        this.components = new Component[partType.getComponentsType().size()];

        List<ComponentType<?>> componentsType = partType.getComponentsType();
        for (int i = 0; i < componentsType.size(); i++)
            this.components[i] = componentsType.get(i).getDefaultInstance();

        partType.handlePartInitialization(this);
    }

    /// Return the component at the index or null if it can't reach the index.
    public Component getComponentByID(int index)
    {
        if (index >= components.length)
            return null;
        return components[index];
    }

    /// Return a list of all components of the type passed in the parameter.
    public <T extends Component> List<T> getComponentByType(ComponentType<T> type)
    {
        return Arrays.stream(components).filter(c -> c.getType() == type).map(c -> (T) c).toList();
    }

    public PartType getPartType()
    {
        return partType;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", partType.id.toString());
        {
            CompoundTag components = new CompoundTag();
            for (int i = 0; i < this.components.length; i++)
                components.put(String.valueOf(i), Component.SERIALIZE(this.components[i], provider));

            components.putInt("size", this.components.length);
            tag.put("components", components);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag)
    {
        PartType type = AmbercraftRegistries.PART_TYPE_REGISTRY.get(ResourceLocation.parse(tag.getString("type")));

        if (type == null)
            throw new IllegalStateException("Unknown part type: " + tag.getString("type"));

        this.partType = type;

        var components_tag = tag.getCompound("components");
        this.components = new Component[components_tag.getInt("size")];//used the size in the data to avoid crash because PartType can change.
        for (int i = 0; i < components.length; i++)
            components[i] = Component.DESERIALIZE(provider, components_tag.getCompound(String.valueOf(i)));
    }
}
