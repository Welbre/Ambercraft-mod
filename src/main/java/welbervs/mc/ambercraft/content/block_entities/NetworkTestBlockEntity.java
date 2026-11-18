package welbervs.mc.ambercraft.content.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.api.component.ComponentSourceCapability;
import welbervs.mc.ambercraft.api.network.NetworkComponent;
import welbervs.mc.ambercraft.api.network.NetworkComponentType;
import welbervs.mc.ambercraft.content.blocks.NetworkTestBlock;
import welbervs.mc.ambercraft.core.registry.BlockEntityRegister;

import java.util.List;

/**
 * Holds the {@link NetworkComponent} of the {@link NetworkTestBlock}. The same instance is returned while the domain
 * doesn't change, which is what the network uses as the node identity.
 */
public class NetworkTestBlockEntity extends BlockEntity
{
    private NetworkComponent component;
    private ComponentSourceCapability capability;

    public NetworkTestBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(BlockEntityRegister.NETWORK_TEST_BLOCK_ENTITY.get(), pos, blockState);
    }

    public @NotNull NetworkComponent getNetworkComponent()
    {
        NetworkComponentType<NetworkComponent> wanted = getBlockState().getValue(NetworkTestBlock.DOMAIN).componentType();
        if (component == null || component.getType() != wanted)
        {
            component = wanted.getDefaultInstance();
            capability = new ComponentSourceCapability(List.of(component));
        }
        return component;
    }

    /// Exposes the component on every side.
    public ComponentSourceCapability getComponentSource(ComponentSourceCapability.Context context)
    {
        getNetworkComponent();
        return capability;
    }
}
