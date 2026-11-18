package welbervs.mc.ambercraft.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import welbervs.mc.ambercraft.api.network.Network;
import welbervs.mc.ambercraft.api.network.NetworkComponent;
import welbervs.mc.ambercraft.api.network.NetworkManager;
import welbervs.mc.ambercraft.content.blocks.NetworkTestBlock;
import welbervs.mc.ambercraft.content.blocks.NetworkTestBlock.TestDomain;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.registry.BlocksRegister;
import welbervs.mc.ambercraft.core.registry.ComponentTypeRegister;

import java.util.List;

/// In-game tests of the network system. Run with {@code gradlew runGameTestServer} or {@code /test runall} in a dev client.
@GameTestHolder(Ambercraft.MODID)
@PrefixGameTestTemplate(false)
public class NetworkGameTests
{
    private static final String TEMPLATE = "empty_5x5x5";

    private static BlockState block(TestDomain domain)
    {
        return BlocksRegister.NETWORK_TEST_BLOCK.get().defaultBlockState().setValue(NetworkTestBlock.DOMAIN, domain);
    }

    private static NetworkComponent node(GameTestHelper helper, BlockPos relative)
    {
        List<NetworkComponent> nodes = NetworkManager.get(helper.getLevel()).getComponentsAt(helper.absolutePos(relative));
        helper.assertTrue(nodes.size() == 1, "Expected 1 component at " + relative + " but found " + nodes.size());
        return nodes.getFirst();
    }

    private static void assertNetwork(GameTestHelper helper, NetworkComponent node, int nodes, int edges)
    {
        Network network = node.getNetwork();
        helper.assertTrue(network != null, node + " has no network");
        helper.assertTrue(network.size() == nodes, "Expected " + nodes + " nodes but found " + network.size());
        int found = network.collectConnections().size();
        helper.assertTrue(found == edges, "Expected " + edges + " edges but found " + found);
    }

    /// Placing blocks in line merges them in one network; removing the middle one splits it.
    @GameTest(template = TEMPLATE)
    public static void lineMergesAndSplits(GameTestHelper helper)
    {
        BlockPos a = new BlockPos(1, 1, 2), b = new BlockPos(2, 1, 2), c = new BlockPos(3, 1, 2);
        helper.startSequence()
                .thenExecute(() -> {
                    helper.setBlock(a, block(TestDomain.ELECTRICAL));
                    helper.setBlock(b, block(TestDomain.ELECTRICAL));
                    helper.setBlock(c, block(TestDomain.ELECTRICAL));
                })
                .thenIdle(2)
                .thenExecute(() -> {
                    NetworkComponent na = node(helper, a), nb = node(helper, b), nc = node(helper, c);
                    helper.assertTrue(na.getNetwork() == nb.getNetwork() && nb.getNetwork() == nc.getNetwork(), "Line isn't a single network");
                    assertNetwork(helper, na, 3, 2);
                    helper.assertTrue(nb.getConnections().size() == 2, "Middle block must have 2 connections");
                })
                .thenExecute(() -> helper.setBlock(b, Blocks.AIR))
                .thenIdle(2)
                .thenExecute(() -> {
                    helper.assertFalse(NetworkManager.get(helper.getLevel()).hasComponentsAt(helper.absolutePos(b)), "Removed block still has components");
                    NetworkComponent na = node(helper, a), nc = node(helper, c);
                    helper.assertTrue(na.getNetwork() != nc.getNetwork(), "Network didn't split");
                    assertNetwork(helper, na, 1, 0);
                    assertNetwork(helper, nc, 1, 0);
                })
                .thenSucceed();
    }

    /// Removing one block of a loop must not split the network.
    @GameTest(template = TEMPLATE)
    public static void loopDoesNotSplit(GameTestHelper helper)
    {
        BlockPos p1 = new BlockPos(1, 1, 1), p2 = new BlockPos(2, 1, 1), p3 = new BlockPos(2, 1, 2), p4 = new BlockPos(1, 1, 2);
        helper.startSequence()
                .thenExecute(() -> {
                    for (BlockPos p : List.of(p1, p2, p3, p4))
                        helper.setBlock(p, block(TestDomain.ELECTRICAL));
                })
                .thenIdle(2)
                .thenExecute(() -> assertNetwork(helper, node(helper, p1), 4, 4))
                .thenExecute(() -> helper.setBlock(p1, Blocks.AIR))
                .thenIdle(2)
                .thenExecute(() -> {
                    NetworkComponent n2 = node(helper, p2), n4 = node(helper, p4);
                    helper.assertTrue(n2.getNetwork() == n4.getNetwork(), "Loop split after removing one block");
                    assertNetwork(helper, n2, 3, 2);
                })
                .thenSucceed();
    }

    /// Different domains don't connect; changing the domain (BlockState) reconnects.
    @GameTest(template = TEMPLATE)
    public static void domainsDoNotMix(GameTestHelper helper)
    {
        BlockPos a = new BlockPos(1, 1, 2), b = new BlockPos(2, 1, 2);
        helper.startSequence()
                .thenExecute(() -> {
                    helper.setBlock(a, block(TestDomain.ELECTRICAL));
                    helper.setBlock(b, block(TestDomain.THERMAL));
                })
                .thenIdle(2)
                .thenExecute(() -> {
                    NetworkComponent na = node(helper, a), nb = node(helper, b);
                    helper.assertTrue(na.getNetwork() != nb.getNetwork(), "Electrical and thermal are in the same network");
                    helper.assertTrue(na.getConnections().isEmpty(), "Electrical connected with thermal");
                })
                .thenExecute(() -> helper.setBlock(b, block(TestDomain.ELECTRICAL)))
                .thenIdle(2)
                .thenExecute(() -> {
                    NetworkComponent na = node(helper, a), nb = node(helper, b);
                    helper.assertTrue(nb.getType() == ComponentTypeRegister.NETWORK_TEST_ELECTRICAL.get(), "Component didn't change type");
                    helper.assertTrue(na.getNetwork() == nb.getNetwork(), "Didn't connect after changing the domain");
                    assertNetwork(helper, na, 2, 1);
                })
                .thenSucceed();
    }

    /// Manual API: add/connect/remove components without blocks.
    @GameTest(template = TEMPLATE)
    public static void manualApi(GameTestHelper helper)
    {
        NetworkManager manager = NetworkManager.get(helper.getLevel());
        NetworkComponent x = ComponentTypeRegister.NETWORK_TEST_ELECTRICAL.get().getDefaultInstance();
        NetworkComponent y = ComponentTypeRegister.NETWORK_TEST_ELECTRICAL.get().getDefaultInstance();
        NetworkComponent t = ComponentTypeRegister.NETWORK_TEST_THERMAL.get().getDefaultInstance();
        // far apart and without exposed sides: only manual connections
        manager.addComponent(helper.absolutePos(new BlockPos(0, 1, 0)), x);
        manager.addComponent(helper.absolutePos(new BlockPos(4, 1, 4)), y);
        manager.addComponent(helper.absolutePos(new BlockPos(4, 1, 0)), t);

        helper.assertTrue(x.getNetwork() != y.getNetwork(), "Not connected yet");
        helper.assertTrue(manager.connect(x, y) != null, "Manual connection refused");
        helper.assertTrue(x.getNetwork() == y.getNetwork(), "Manual connection didn't merge");
        helper.assertTrue(manager.connect(x, t) == null, "Rule must refuse electrical <-> thermal");

        manager.removeComponent(y);
        helper.assertFalse(y.isInNetwork(), "Removed component still in a network");
        helper.assertTrue(x.getConnections().isEmpty() && x.getNetwork().size() == 1, "Removal didn't update the other side");

        manager.removeComponent(x);
        manager.removeComponent(t);
        helper.succeed();
    }
}
