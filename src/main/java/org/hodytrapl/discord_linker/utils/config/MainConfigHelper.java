package org.hodytrapl.discord_linker.utils.config;


import org.hodytrapl.discord_linker.config.general.MainConfig;
import org.hodytrapl.discord_linker.config.general.MainEntryConfig;

import java.util.Map;

import static org.hodytrapl.discord_linker.utils.Utils.formatPlaceholder;

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

    public static Boolean getEnableField(MainEntryConfig event) {
        return event.enable.get();
    }
    public static String getChannelIdField(MainEntryConfig event) {
        return event.channelId.get();
    }
    public static Integer getUpdateIntervalField(MainEntryConfig event) {
        return event.updateInterval.get();
    }
    // ==================== Сырые (raw) геттеры – без подстановки ====================

    public static String getRawNameChannel(MainEntryConfig event) {
        return event.name_channel.get();
    }

    // ==================== Форматированные (formatted) геттеры – с подстановкой ====================

    public static String getFormattedNameChannelField(MainEntryConfig event, Map<String, String> placeholders) {
        return formatPlaceholder(event.name_channel.get(), placeholders);
    }
    public static String getFormattedNameChannelField(MainEntryConfig event, String... pairs) {
        return formatPlaceholder(event.name_channel.get(), pairs);
    }
}