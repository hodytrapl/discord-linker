package org.hodytrapl.discord_linker.utils.config;

import org.hodytrapl.discord_linker.config.general.MainConfig;

public class MainConfigHelper {

    public static String getLang() {
        return MainConfig.INSTANCE.langSelect.get();
    }

    public static boolean isBotEnabled() {
        return MainConfig.INSTANCE.enableBot.get();
    }

    public static String getBotToken() {
        return MainConfig.INSTANCE.botToken.get();
    }

    public static String getRawChannelId() {
        return MainConfig.INSTANCE.channelID.get();
    }

    public static String getRawEventsId() {
        return MainConfig.INSTANCE.eventsID.get();
    }

    public static String getRawCommandsId() {
        return MainConfig.INSTANCE.commandsID.get();
    }

    public static boolean isPresenceEnabled() {
        return MainConfig.INSTANCE.enablePresence.get();
    }

    public static String getPresenceMessage() {
        return MainConfig.INSTANCE.presenceMessage.get();
    }
}