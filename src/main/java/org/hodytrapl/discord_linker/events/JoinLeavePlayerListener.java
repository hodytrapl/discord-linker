package org.hodytrapl.discord_linker.events;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.hodytrapl.discord_linker.Discord_linker;
import org.hodytrapl.discord_linker.discord.DiscordBotManager;
import org.hodytrapl.discord_linker.discord.enums.DiscordMessageType;
import org.hodytrapl.discord_linker.utils.config.EventChannelHelper;
import org.hodytrapl.discord_linker.utils.config.EventsConfigHelper;
import org.slf4j.Logger;

@EventBusSubscriber(modid = "discord_linker")
public class JoinLeavePlayerListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // First check if any events channel is available
        if (!EventChannelHelper.areEventsEnabled()) return;
        // Then check if the specific event is enabled in config
        if (!EventsConfigHelper.isPlayerJoinEnabled()) return;

        Player player = event.getEntity();
        String username = player.getName().getString();
        String message = EventChannelHelper.formatPlayerJoinMessage(username);

        DiscordBotManager botManager = Discord_linker.getBotManager();
        String channelId = EventChannelHelper.getEventsChannelId();

        if (channelId != null) {
            // Optional: check if embed is enabled for join event
            if (EventsConfigHelper.useEmbedForPlayerJoin()) {
                // TODO: implement sendEmbed method in DiscordBotManager
                LOGGER.debug("Embed for join event not yet implemented, sending plain text");
                botManager.sendMessage(channelId, message, DiscordMessageType.CHAT_MESSAGE);
            } else {
                botManager.sendMessage(channelId, message, DiscordMessageType.CHAT_MESSAGE);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!EventChannelHelper.areEventsEnabled()) return;
        if (!EventsConfigHelper.isPlayerLeaveEnabled()) return;

        Player player = event.getEntity();
        String username = player.getName().getString();
        String message = EventChannelHelper.formatPlayerLeaveMessage(username);

        DiscordBotManager botManager = Discord_linker.getBotManager();
        String channelId = EventChannelHelper.getEventsChannelId();

        if (channelId != null) {
            botManager.sendMessage(channelId, message, DiscordMessageType.CHAT_MESSAGE);
        }
    }
}