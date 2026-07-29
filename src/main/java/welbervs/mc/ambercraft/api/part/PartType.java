package welbervs.mc.ambercraft.api.part;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.List;
import java.util.function.Supplier;

public abstract class PartType
{
    public final ResourceLocation id;
    private final Supplier<Part> defaultInstance;
    private final List<ComponentType<?>> componentsType;

    /**
     * Creates a new IPartType
     *
     * @param id              The resourceLocation where the IPartType will be registered
     * @param defaultInstance A supplier that returns the default IPart.
     */
    public PartType(@NotNull ResourceLocation id, @NotNull Supplier<Part> defaultInstance, @NotNull List<ComponentType<?>> componentsType)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
        this.componentsType = List.copyOf(componentsType);
    }

    public PartType(@NotNull ResourceLocation id)
    {
        this.id = id;
        this.defaultInstance = () -> new Part(this);
        this.componentsType = List.of();
    }

    public abstract VoxelShape getShape(Part part);

    public List<ComponentType<?>> getComponentsType()
    {
        return componentsType;
    }

    public Part getDefaultInstance()
    {
        return defaultInstance.get();
    }
}
