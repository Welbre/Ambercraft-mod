package welbervs.mc.ambercraft.api.part;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class PartType
{
    public final ResourceLocation id;
    private final Supplier<Part> defaultInstance;
    private final List<Holder<ComponentType<?>>> componentsType;

    /**
     * Creates a new PartType. <a color=#ffff33>Used only in special cases, prefer</a> {@link #PartType(ResourceLocation, Holder[])}
     *
     * @param id              The resourceLocation where the PartType will be registered.
     * @param defaultInstance A supplier that returns the default Part. Useful to create a Part with extra data.
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

    /// Called after the Part constructor initializes the all Part essencial stuff.
    public abstract void handlePartInitialization(Part part);

    /// Called when someone is try to get components from an BlockEntity.<br> The method's parathmeres is used to decide what should be returned.
    public abstract Collection<? extends Component> handleGetComponentByContext(Part part, BlockEntity be, ComponentSourceCapability.Context context);

    public abstract @NotNull VoxelShape getShape(Part part);

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
