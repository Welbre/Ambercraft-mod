package welbervs.mc.ambercraft.api.ipart;

import net.minecraft.world.phys.shapes.VoxelShape;

public interface IPart
{
    void setShape(VoxelShape shape);

    VoxelShape getShape();
}
