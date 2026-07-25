package welbervs.mc.ambercraft.core;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import welbervs.mc.ambercraft.core.registry.DataComponentRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Ambercraft.MODID)
public class Ambercraft
{
    public static final String MODID = "ambercraft";

    public static final Logger LOGGER = LogUtils.getLogger();






    public Ambercraft(IEventBus modEventBus, ModContainer modContainer)
    {
        //Mod bus
        modEventBus.addListener(this::commonSetup);

        //NeoForge bus
        NeoForge.EVENT_BUS.register(this);

        //Registers
        welbervs.mc.ambercraft.core.registry.BlocksRegister.REGISTER.register(modEventBus);
        welbervs.mc.ambercraft.core.registry.ItemsRegister.REGISTER.register(modEventBus);
        welbervs.mc.ambercraft.core.registry.CreativeTabRegister.REGISTER.register(modEventBus);
        welbervs.mc.ambercraft.core.registry.BlockEntityRegister.REGISTER.register(modEventBus);
        DataComponentRegister.REGISTER.register(modEventBus);


        // Config files
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
