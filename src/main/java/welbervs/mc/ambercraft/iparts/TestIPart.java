package welbervs.mc.ambercraft.iparts;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import welbervs.mc.ambercraft.api.ipart.IPart;

public class TestIPart implements IPart
{
    public VoxelShape shape;
    public final int color;

    public TestIPart(int color)
    {
        this.shape = Shapes.box(0,0,0,1,0.5,1);
        this.color = color;
    }

    @Override
    public void setShape(VoxelShape shape)
    {
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape()
    {
        return shape;
    }
}
