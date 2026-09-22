package welbervs.mc.ambercraft.content.part_type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.part.Part;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.content.components.ElectricalResistenceComponent;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class ResistorPartType extends PartType
{
    //todo fazer outra parte para teste, e renomear para ficar mais palpavel
    public ResistorPartType(ResourceLocation id)
    {
        super(id, ComponentTypeRegister.HEAT_DISSIPATOR, ComponentTypeRegister.ELECTRICAL_RESISTENCE);
    }

    @Override
    public void handlePartInitialization(Part part)
    {
        part.getComponentByType(ComponentTypeRegister.HEAT_DISSIPATOR.get()).getFirst().setResistance(100);
        if (part.getComponentByID(1) instanceof ElectricalResistenceComponent resis)
        {
            resis.setResistance(50);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(Part part)
    {
        return null;
    }
}
