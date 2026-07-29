package welbervs.mc.ambercraft.content.part_type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import welbervs.mc.ambercraft.api.part.PartInstance;
import welbervs.mc.ambercraft.api.part.Part;

public class TestPart extends Part
{
    public TestPart(ResourceLocation id)
    {
        super(id);
    }

    @Override
    public VoxelShape getShape(PartInstance partInstance)
    {
        return null;
    }
}
