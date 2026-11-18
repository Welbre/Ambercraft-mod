package welbervs.mc.ambercraft.content.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import welbervs.mc.ambercraft.api.network.Network;
import welbervs.mc.ambercraft.api.network.NetworkComponent;
import welbervs.mc.ambercraft.api.network.NetworkComponentType;
import welbervs.mc.ambercraft.api.network.NetworkManager;
import welbervs.mc.ambercraft.content.block_entities.NetworkTestBlockEntity;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

import java.util.List;

/**
 * Debug block that exposes one {@link NetworkComponent} on all 6 sides.
 * <ul>
 *     <li>Right-click (empty hand): prints the network info of the block.</li>
 *     <li>Shift + right-click (empty hand): cycles the domain (electrical/thermal). Only blocks of the same domain connect.</li>
 * </ul>
 */
public class NetworkTestBlock extends Block implements EntityBlock
{
    public static final EnumProperty<TestDomain> DOMAIN = EnumProperty.create("domain", TestDomain.class);

    public NetworkTestBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(DOMAIN, TestDomain.ELECTRICAL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder)
    {
        builder.add(DOMAIN);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
    {
        return new NetworkTestBlockEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult)
    {
        if (!(level instanceof ServerLevel serverLevel))
            return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown())
        {
            BlockState newState = state.cycle(DOMAIN);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
            player.displayClientMessage(Component.literal("Domain -> " + newState.getValue(DOMAIN).getSerializedName()).withStyle(ChatFormatting.YELLOW), true);
        }
        else
            describe(serverLevel, pos, player);

        return InteractionResult.CONSUME;
    }

    private static void describe(ServerLevel level, BlockPos pos, Player player)
    {
        List<NetworkComponent> nodes = NetworkManager.get(level).getComponentsAt(pos);
        player.sendSystemMessage(Component.literal("[Network] " + pos.toShortString() + " - " + nodes.size() + " component(s)").withStyle(ChatFormatting.GOLD));

        for (NetworkComponent node : nodes)
        {
            Network network = node.getNetwork();
            MutableComponent line = Component.literal(" - " + node.getType().id + " (" + node.getDomain() + ")")
                    .append(Component.literal(" sides=" + Integer.bitCount(node.getExposedSides())))
                    .append(Component.literal(" links=" + node.getConnections().size()));
            if (network != null)
                line.append(Component.literal(" | network #" + network.getId() + ": " + network.size() + " nodes, "
                        + network.collectConnections().size() + " edges, v" + network.getVersion()).withStyle(ChatFormatting.AQUA));
            player.sendSystemMessage(line);

            StringBuilder links = new StringBuilder("   connected to:");
            node.getConnections().forEach(c -> {
                Direction side = c.sideFrom(node);
                links.append(' ').append(side == null ? "manual" : side.getName());
            });
            if (!node.getConnections().isEmpty())
                player.sendSystemMessage(Component.literal(links.toString()).withStyle(ChatFormatting.GRAY));
        }
    }

    public enum TestDomain implements StringRepresentable
    {
        ELECTRICAL("electrical"),
        THERMAL("thermal");

        private final String name;

        TestDomain(String name)
        {
            this.name = name;
        }

        public NetworkComponentType<NetworkComponent> componentType()
        {
            return switch (this)
            {
                case ELECTRICAL -> ComponentTypeRegister.NETWORK_TEST_ELECTRICAL.get();
                case THERMAL -> ComponentTypeRegister.NETWORK_TEST_THERMAL.get();
            };
        }

        @Override
        public @NotNull String getSerializedName()
        {
            return name;
        }
    }
}
