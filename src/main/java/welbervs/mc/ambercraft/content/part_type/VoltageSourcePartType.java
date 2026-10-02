package welbervs.mc.ambercraft.content.part_type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.api.part.Part;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

import java.util.Collection;

public class VoltageSourcePartType extends PartType
{
    public VoltageSourcePartType(@NotNull ResourceLocation id)
    {
        super(id, ComponentTypeRegister.ELECTRICAL_SOURCE, ComponentTypeRegister.HEAT_DISSIPATOR);
    }

    @Override
    public void handlePartInitialization(Part part)
    {
        part.getComponentByType(ComponentTypeRegister.ELECTRICAL_SOURCE.get()).getFirst().setVoltage(1000);
        part.getComponentByType(ComponentTypeRegister.HEAT_DISSIPATOR.get()).getFirst().setResistance(10);
    }

    @Override
    public Collection<? extends Component> handleGetComponentByContext(Part part, BlockEntity be, ComponentSourceCapability.Context context)
    {
        return part.getAllComponents();
    }

    @Override
    public @NotNull VoxelShape getShape(Part part)
    {
        return Shapes.empty();
    }
}
