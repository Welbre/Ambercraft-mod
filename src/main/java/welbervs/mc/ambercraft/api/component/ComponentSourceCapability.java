package welbervs.mc.ambercraft.api.component;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import welbervs.mc.ambercraft.api.part.Part;

import java.util.List;

public record ComponentSourceCapability(List<Component> components)
{
    /**
     * Context that someone is trying to get a capability from a ComponentSourceCapability.
     * The context is important to differentiate how to get the capability from a block/block entity. Like getting a connection point that is only available in one direction.
     **/
    public record Context(@Nullable Direction direction, @Nullable Part part)
    {
        public static Context EMPTY = new Context(null, null);
    }


    public class ContextBuilder
    {
        private Direction direction = null;
        private Part part = null;

        public ContextBuilder withDirection(Direction direction)
        {
            this.direction = direction;
            return this;
        }

        public ContextBuilder withPart(Part part)
        {
            this.part = part;
            return this;
        }

        public Context build()
        {
            return new Context(direction, part);
        }
    }

    public ComponentSourceCapability()
    {
        this(List.of());
    }
}
