package welbervs.mc.ambercraft.api.part;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.List;
import java.util.function.Supplier;

public abstract class Part
{
    public final ResourceLocation id;
    private final Supplier<PartInstance> defaultInstance;
    private final List<ComponentType<?>> componentsType;

    /**
     * Creates a new IPartType
     *
     * @param id              The resourceLocation where the IPartType will be registered
     * @param defaultInstance A supplier that returns the default IPart.
     */
    public Part(@NotNull ResourceLocation id, @NotNull Supplier<PartInstance> defaultInstance, @NotNull List<ComponentType<?>> componentsType)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
        this.componentsType = List.copyOf(componentsType);
    }

    public Part(@NotNull ResourceLocation id)
    {
        this.id = id;
        this.defaultInstance = () -> new PartInstance(this);
        this.componentsType = List.of();
    }

    public abstract VoxelShape getShape(PartInstance partInstance);

    public List<ComponentType<?>> getComponentsType()
    {
        return componentsType;
    }

    public PartInstance getDefaultInstance()
    {
        return defaultInstance.get();
    }
}
