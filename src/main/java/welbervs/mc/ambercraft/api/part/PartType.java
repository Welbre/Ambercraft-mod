package welbervs.mc.ambercraft.api.part;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class PartType
{
    public final ResourceLocation id;
    private final Supplier<Part> defaultInstance;
    private final List<Holder<ComponentType<?>>> componentsType;

    /**
     * Creates a new PartType
     *
     * @param id              The resourceLocation where the PartType will be registered.
     * @param defaultInstance A supplier that returns the default Part.
     * @param componentsType  The list of components that will be used in the Part.
     */
    @SafeVarargs
    public PartType(@NotNull ResourceLocation id, @NotNull Supplier<Part> defaultInstance, @NotNull Holder<ComponentType<?>>... componentsType)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
        this.componentsType = List.copyOf(List.of(componentsType));
    }

    /**
     * Creates a new PartType
     *
     * @param id              The resourceLocation where the PartType will be registered.
     * @param componentsType  The list of components that will be used in the Part.
     */
    @SafeVarargs
    public PartType(@NotNull ResourceLocation id, @NotNull Holder<ComponentType<?>>... componentsType)
    {
        this.id = id;
        this.defaultInstance = () -> new Part(this);
        this.componentsType = List.copyOf(List.of(componentsType));
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
        var list = new ArrayList<ComponentType<?>>(componentsType.size());
        for (Holder<ComponentType<?>> holder : componentsType)
            list.add(holder.value());
        return list;
    }

    public Part getDefaultInstance()
    {
        return defaultInstance.get();
    }
}
