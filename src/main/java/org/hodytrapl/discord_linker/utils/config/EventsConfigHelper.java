package org.hodytrapl.discord_linker.utils.config;

import org.hodytrapl.discord_linker.config.events.EventEntryConfig;
import org.hodytrapl.discord_linker.config.events.EventsConfig;
import java.util.List;

public class EventsConfigHelper {

    // --- главный конфиг событий ---


    // --- включены ли ивенты ---
    public static boolean isEventEnabled(EventEntryConfig event) {
        return event.eventEnable.get();
    }

    //сообщения без форматирования текста
    public static String getRawEventMessage(EventEntryConfig event) {
        return event.eventMessage.get();
    }

    // --- Геттеры для embed --- ---
    public static boolean useEmbedForEvent(EventEntryConfig event) {
        return event.embedEnable.get();
    }

    public static String getEmbedAuthorName(EventEntryConfig event) {
        return event.embedAuthorName.get();
    }

    public static String getEmbedAuthorIcon(EventEntryConfig event) {
        return event.embedAuthorIcon.get();
    }

    public static String getEmbedTitle(EventEntryConfig event) {
        return event.embedTitle.get();
    }

    public static String getEmbedDescription(EventEntryConfig event) {
        return event.embedDescription.get();
    }

    public static String getEmbedColor(EventEntryConfig event) {
        return event.embedColor.get();
    }

    public static String getEmbedThumbnail(EventEntryConfig event) {
        return event.embedThumbnail.get();
    }

    public static String getEmbedImage(EventEntryConfig event) {
        return event.embedImage.get();
    }

    public static String getEmbedFooterIcon(EventEntryConfig event) {
        return event.embedFooterIcon.get();
    }

    public static String getEmbedFooter(EventEntryConfig event) {
        return event.embedFooter.get();
    }

    public static boolean isEmbedTimestampEnabled(EventEntryConfig event) {
        return event.embedOnTimestamp.get();
    }

    public static List<? extends String> getEmbedFields(EventEntryConfig event) {
        return event.embedFields.get();
    }

    // --- Удобные методы с заменой плейсхолдеров для embed ---
    public static String getEmbedTitleWithUsername(EventEntryConfig event, String username) {
        return replaceUsername(getEmbedTitle(event), username);
    }

    public static String getEmbedDescriptionWithUsername(EventEntryConfig event, String username) {
        return replaceUsername(getEmbedDescription(event), username);
    }

    // --- замена плейсхолдеров ---
    public static String replaceUsername(String template, String username) {
        return template.replace("%username%", username);
    }

    public static String replaceHeadPlayer(String template, String username) {
        return template.replace("%headplayer%", "https://minotar.net/avatar/"+username+"/64");
    }
}