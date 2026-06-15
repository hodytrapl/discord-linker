package org.hodytrapl.discord_linker.utils.config;

import org.hodytrapl.discord_linker.config.events.EventEntryConfig;
import org.hodytrapl.discord_linker.config.events.EventsConfig;
import java.util.List;

public class EventsConfigHelper {

    // --- Raw message templates (still contain %username% placeholder) ---
    public static String getRawPlayerJoinMessage() {
        return EventsConfig.INSTANCE.playerJoin.eventMessage.get();
    }

    public static String getRawPlayerLeaveMessage() {
        return EventsConfig.INSTANCE.playerLeave.eventMessage.get();
    }

    public static String getRawServerStartedMessage() {
        return EventsConfig.INSTANCE.serverStarted.eventMessage.get();
    }

    public static String getRawServerStoppedMessage() {
        return EventsConfig.INSTANCE.serverStopped.eventMessage.get();
    }

    public static String getRawServerCrashedMessage() {
        return EventsConfig.INSTANCE.serverCrashed.eventMessage.get();
    }

    // --- Enable/disable per event ---
    public static boolean isPlayerJoinEnabled() {
        return EventsConfig.INSTANCE.playerJoin.eventEnable.get();
    }

    public static boolean isPlayerLeaveEnabled() {
        return EventsConfig.INSTANCE.playerLeave.eventEnable.get();
    }

    public static boolean isServerStartedEnabled() {
        return EventsConfig.INSTANCE.serverStarted.eventEnable.get();
    }

    public static boolean isServerStoppedEnabled() {
        return EventsConfig.INSTANCE.serverStopped.eventEnable.get();
    }

    public static boolean isServerCrashedEnabled() {
        return EventsConfig.INSTANCE.serverCrashed.eventEnable.get();
    }

    // --- Embed enable and full config objects ---
    public static boolean useEmbedForPlayerJoin() {
        return EventsConfig.INSTANCE.playerJoin.embedEnable.get();
    }

    public static boolean useEmbedForPlayerLeave() {
        return EventsConfig.INSTANCE.playerLeave.embedEnable.get();
    }

    public static EventEntryConfig getPlayerJoinConfig() {
        return EventsConfig.INSTANCE.playerJoin;
    }

    public static EventEntryConfig getPlayerLeaveConfig() {
        return EventsConfig.INSTANCE.playerLeave;
    }

    // --- Placeholder replacement ---
    public static String replaceUsername(String template, String username) {
        return template.replace("%username%", username);
    }
}