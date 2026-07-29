package welbervs.mc.ambercraft.api.component;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ComponentType<T extends Component>
{
    public final ResourceLocation id;
    private final Supplier<T> defaultInstance;

    public ComponentType(ResourceLocation id, Supplier<T> defaultInstance)
    {
        this.id = id;
        this.defaultInstance = defaultInstance;
    }

    public T getDefaultInstance()
    {
        return defaultInstance.get();
    }
}
