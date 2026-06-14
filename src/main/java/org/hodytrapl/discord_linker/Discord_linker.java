package org.hodytrapl.discord_linker;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.hodytrapl.discord_linker.config.ConfigManager;
import org.hodytrapl.discord_linker.events.JoinLeavePlayerListener;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Discord_linker.MODID)
public class Discord_linker {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "discord_linker";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Discord_linker(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        new ConfigManager(modContainer);

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Discord linker started");
    }
    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        // Do something when the server starts
        LOGGER.info("Discord linker stopped");
    }
}
