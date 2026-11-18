package welbervs.mc.ambercraft.core.network;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import welbervs.mc.ambercraft.api.network.NetworkManager;
import welbervs.mc.ambercraft.core.Ambercraft;

/// Connects the {@link NetworkManager} with the level lifecycle. Block changes come from {@code ServerLevelMixin}.
@EventBusSubscriber(modid = Ambercraft.MODID)
public class NetworkEvents
{
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        NetworkManager manager = NetworkManager.getIfPresent(event.getLevel());
        if (manager != null)
            manager.tick();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event)
    {
        // Only schedules the refresh, the chunk may not be FULL yet.
        if (event.getLevel() instanceof ServerLevel level)
            NetworkManager.get(level).onChunkLoaded(event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel level)
        {
            NetworkManager manager = NetworkManager.getIfPresent(level);
            if (manager != null)
                manager.onChunkUnloaded(event.getChunk().getPos());
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel level)
            NetworkManager.onLevelUnloaded(level);
    }
}
