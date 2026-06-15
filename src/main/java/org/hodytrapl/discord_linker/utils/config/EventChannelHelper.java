package org.hodytrapl.discord_linker.utils.config;

import org.hodytrapl.discord_linker.utils.ValidationUtils;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class EventChannelHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    private EventChannelHelper() {}

    /**
     * Resolves the effective channel ID for events.
     * Priority: eventsID (if valid) → channelID (if valid) → null (disabled)
     */
    public static String getEventsChannelId() {
        String eventsId = MainConfigHelper.getRawEventsId();
        if (ValidationUtils.isValidId(eventsId)) {
            return eventsId;
        }
        String fallback = MainConfigHelper.getRawChannelId();
        if (ValidationUtils.isValidId(fallback)) {
            LOGGER.info("eventsID not set/invalid, using fallback channelID for events");
            return fallback;
        }
        LOGGER.warn("No valid channel ID for events – event messages will be disabled");
        return null;
    }

    /**
     * Quick check if events can be sent at all (valid channel exists).
     */
    public static boolean areEventsEnabled() {
        return getEventsChannelId() != null;
    }

    /**
     * Returns formatted join message with %username% replaced.
     */
    public static String formatPlayerJoinMessage(String username) {
        String template = EventsConfigHelper.getRawPlayerJoinMessage();
        return template.replace("%username%", username);
    }

    /**
     * Returns formatted leave message with %username% replaced.
     */
    public static String formatPlayerLeaveMessage(String username) {
        String template = EventsConfigHelper.getRawPlayerLeaveMessage();
        return template.replace("%username%", username);
    }

    /**
     * Returns formatted server started message.
     */
    public static String getServerStartedMessage() {
        return EventsConfigHelper.getRawServerStartedMessage();
    }

    public static String getServerStoppedMessage() {
        return EventsConfigHelper.getRawServerStoppedMessage();
    }

    public static String getServerCrashedMessage() {
        return EventsConfigHelper.getRawServerCrashedMessage();
    }
}