package org.hodytrapl.discord_linker.discord;

import com.mojang.logging.LogUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraft.server.MinecraftServer;
import org.hodytrapl.discord_linker.Discord_linker;
import org.hodytrapl.discord_linker.discord.commands.CommandListener;
import org.hodytrapl.discord_linker.discord.enums.DiscordMessageType;
import org.hodytrapl.discord_linker.discord.events.ChannelUpdateService;
import org.hodytrapl.discord_linker.discord.events.MessageListener;
import org.hodytrapl.discord_linker.utils.config.MainConfigHelper;
import org.slf4j.Logger;


import static org.hodytrapl.discord_linker.LanguageManager.getMessage;
import static org.hodytrapl.discord_linker.utils.ValidationUtils.isValidId;

public class DiscordBotManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private JDA jda;
    private boolean initialized = false;
    private final Object lock = new Object();
    private static MinecraftServer currentServer;
    private ChannelUpdateService channelUpdateService;

    public void initializeBot(MinecraftServer server) {
        synchronized (lock) {
            if (initialized) return;
            if (!MainConfigHelper.isBotEnabled()) return;
            String token = MainConfigHelper.getBotToken();
            if (token.equals("INSERT_BOT_TOKEN_HERE") || token.equals("BOT_TOKEN_HERE")) {
                LOGGER.warn(getMessage("mod.typelogger.discord.bot.dontconfigurated"));
                return;
            }

            try {
                JDABuilder builder = JDABuilder.createDefault(token);
                builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
                builder.addEventListeners(new CommandListener());

                jda = builder.build().awaitReady();
                setBotPresence();
                initialized = true;
                currentServer = server;
                jda.addEventListener(new MessageListener());

                channelUpdateService = ChannelUpdateService.getInstance();
                channelUpdateService.initialize(jda);

                LOGGER.info(getMessage("mod.typelogger.discord.bot.loggedin",jda.getSelfUser().getName()));
            } catch (Exception e) {
                LOGGER.error(getMessage("mod.typelogger.discord.bot.startfailed",e.getMessage()));
            }
        }
    }

    public JDA getJda() {
        return jda;
    }

    public ChannelUpdateService getChannelUpdateService() {
        return channelUpdateService;
    }

    public void shutdown() {
        if (channelUpdateService != null) {
            channelUpdateService.shutdown();
        }
        if (jda != null) {
            jda.shutdown();
            jda = null;
        }
        initialized = false;
    }

    public void reloadBot() {
        LOGGER.info(getMessage("mod.typelogger.discord.bot.reloading"));
        synchronized (lock) {
            if (channelUpdateService != null) {
                channelUpdateService.reload();
            }
            shutdown();
            // Небольшая задержка для корректного завершения
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            initializeBot(currentServer);
        }
    }
    public void sendMessage(String channelId, String content, DiscordMessageType type) {
        if (jda == null) {
            LOGGER.warn(getMessage("mod.typelogger.discord.message.notinitialized"));
            return;
        }

        // 2. Validate channel ID using the same logic as CommandListener
        if (!isValidId(channelId)) {
            LOGGER.warn(getMessage("mod.typelogger.discord.message.invalidchannel",channelId));
            return;
        }

        // 3. Handle null/empty content
        if (content == null) content = "";
        if (content.isEmpty()) {
            LOGGER.debug(getMessage("mod.typelogger.discord.message.emptycontent"));
            return;
        }

        // 1. Apply formatting based on type
        String formatted;
        switch (type) {
            case COMMAND_RESPONSE:
                formatted = "```\n" + content + "\n```";
                break;
            case CHAT_MESSAGE:
                formatted = content;
                break;
            case SYSTEM_NOTIFICATION:
                formatted = "[System] " + content;
                break;
            default:
                formatted = content;
        }

        String trimmed = formatted.length() > 1900 ? formatted.substring(0, 1900) + "..." : formatted;

        jda.getTextChannelById(channelId)
                .sendMessage(trimmed)
                .queue(null, failure -> LOGGER.error(getMessage("mod.typelogger.discord.message.sendfailed",failure)));
    }

    private void setBotPresence() {
        if (jda != null && MainConfigHelper.isPresenceEnabled()) {
            String presenceMessage = MainConfigHelper.getPresenceMessage();
            jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing(presenceMessage));
            LOGGER.info(getMessage("mod.typelogger.discord.presence.set",presenceMessage));
        }
    }

    public void sendEmbed(String channelId, MessageEmbed embed) {
        if (jda == null || !isValidId(channelId)) {
            LOGGER.warn(getMessage("mod.typelogger.discord.embed.notready"));
            return;
        }
        var channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            LOGGER.warn(getMessage("mod.typelogger.discord.channel.notfound",channelId));
            return;
        }
        channel.sendMessageEmbeds(embed).queue(
                null,
                failure -> LOGGER.error(getMessage("mod.typelogger.discord.embed.sendfailed", failure))
        );
    }

    public void sendMessageSync(String channelId, String content, DiscordMessageType type) {
        if (jda == null) {
            LOGGER.warn(getMessage("mod.typelogger.discord.message.notinitialized"));
            return;
        }
        if (!isValidId(channelId)) {
            LOGGER.warn(getMessage("mod.typelogger.discord.channel.notfound", channelId));
            return;
        }
        if (content == null) content = "";
        if (content.isEmpty()) return;

        String formatted;
        switch (type) {
            case COMMAND_RESPONSE:
                formatted = "```\n" + content + "\n```";
                break;
            case SYSTEM_NOTIFICATION:
                formatted = "[System] " + content;
                break;
            default:
                formatted = content;
        }
        String trimmed = formatted.length() > 1900 ? formatted.substring(0, 1900) + "..." : formatted;

        var channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            LOGGER.warn(getMessage("mod.typelogger.discord.channel.notfound", channelId));
            return;
        }
        try {
            channel.sendMessage(trimmed).complete(); // синхронно!
            LOGGER.info(getMessage("mod.typelogger.discord.embed.sentsync",channelId));
        } catch (Exception e) {
            LOGGER.error(getMessage("mod.typelogger.discord.message.syncfailed",e));
        }
    }

    public void sendEmbedSync(String channelId, MessageEmbed embed) {
        if (jda == null || !isValidId(channelId)) {
            LOGGER.warn(getMessage("mod.typelogger.discord.embed.syncnotready"));
            return;
        }
        var channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            LOGGER.warn(getMessage("mod.typelogger.discord.channel.notfound", channelId));
            return;
        }
        try {
            channel.sendMessageEmbeds(embed).complete(); // синхронно!
            LOGGER.info(getMessage("mod.typelogger.discord.embed.sentsync",channelId));
        } catch (Exception e) {
            LOGGER.error(getMessage("mod.typelogger.discord.message.syncfailed",e));
        }
    }
}
