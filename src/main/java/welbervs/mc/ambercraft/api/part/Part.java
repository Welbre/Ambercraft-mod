package welbervs.mc.ambercraft.api.part;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;
import welbervs.mc.ambercraft.api.registry.AmbercraftRegistries;

import java.util.List;

public final class Part
{
    private Component[] component;
    private PartType partType;

    public Part()
    {
    }

    public Part(PartType partType)
    {
        this.partType = partType;
        this.component = new Component[partType.getComponentsType().size()];

        initializeComponents(partType);
    }

    private void initializeComponents(PartType partType)
    {
        List<ComponentType<?>> componentsType = partType.getComponentsType();
        for (int i = 0; i < componentsType.size(); i++)
            this.component[i] = componentsType.get(i).getDefaultInstance();
    }

    public PartType getPartType()
    {
        return partType;
    }


    public void saveAdditional(CompoundTag tag)
    {
        tag.putString("type", partType.id.toString());
    }

    public void load(CompoundTag tag)
    {
        Registry<PartType> registry = (Registry<PartType>) BuiltInRegistries.REGISTRY.get(AmbercraftRegistries.PART_TYPE.location());
        PartType type = registry.get(ResourceLocation.parse(tag.getString("type")));
        if (type == null)
            throw new IllegalStateException("Unknown part type: " + tag.getString("type"));

        this.partType = type;
        this.component = new Component[type.getComponentsType().size()];
        initializeComponents(type);
    }
}
