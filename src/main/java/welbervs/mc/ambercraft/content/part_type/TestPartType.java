package welbervs.mc.ambercraft.content.part_type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import welbervs.mc.ambercraft.api.part.Part;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

public class TestPartType extends PartType
{
    public TestPartType(ResourceLocation id)
    {
        super(id, ComponentTypeRegister.TEST_COMPONENT_TYPE);
    }

    @Override
    public VoxelShape getShape(Part part)
    {
        return null;
    }
}
