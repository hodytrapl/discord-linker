package org.hodytrapl.discord_linker.events;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import org.slf4j.Logger;

@EventBusSubscriber(modid = "discord_linker")
public class JoinLeavePlayerListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public static void OnPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        LOGGER.info("{} hello!",player.getName().getString());
    }
    @SubscribeEvent
    public static void OnPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        LOGGER.info("{} hello!",player.getName().getString());
    }
}
