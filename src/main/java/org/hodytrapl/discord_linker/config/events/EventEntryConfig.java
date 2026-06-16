package org.hodytrapl.discord_linker.config.events;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;

public class EventEntryConfig {
    // Основные настройки события
    public final ModConfigSpec.BooleanValue eventEnable;
    public final ModConfigSpec.ConfigValue<String> eventMessage;
    public final ModConfigSpec.BooleanValue embedEnable;

    // Автор embed
    public final ModConfigSpec.ConfigValue<String> embedAuthorName;
    public final ModConfigSpec.ConfigValue<String> embedAuthorIcon;

    // Основные поля embed
    public final ModConfigSpec.ConfigValue<String> embedTitle;
    public final ModConfigSpec.ConfigValue<String> embedDescription;
    public final ModConfigSpec.ConfigValue<String> embedColor;
    public final ModConfigSpec.ConfigValue<String> embedThumbnail;
    public final ModConfigSpec.ConfigValue<String> embedImage;
    public final ModConfigSpec.ConfigValue<String> embedFooterIcon;
    public final ModConfigSpec.ConfigValue<String> embedFooter;
    public final ModConfigSpec.BooleanValue embedOnTimestamp;

    // Динамические поля
    public final ModConfigSpec.ConfigValue<List<? extends String>> embedFields;

    @SuppressWarnings("deprecation")
    public EventEntryConfig(ModConfigSpec.Builder builder, String eventName, String message, String description) {
        builder.comment("Configuration for " + eventName).push(eventName);

        eventEnable = builder
                .comment("Enable/disable this event")
                .define("event_enable", true);

        eventMessage = builder
                .comment("Plain text message (supports %username% placeholder)")
                .define("message", message);

        embedEnable = builder
                .comment("Send an embed message to Discord instead of plain text")
                .define("embed_enable", false);

        // --- Настройки embed (используются только если embed_enable = true) ---
        builder.comment("Embed appearance (Discord rich embed)").push("embed");

        embedAuthorName = builder
                .comment("Author name displayed at the top of embed")
                .define("author_name", "");

        embedAuthorIcon = builder
                .comment("Author icon URL (optional)")
                .define("author_icon_url", "");

        embedTitle = builder
                .comment("Embed title (supports %username%)")
                .define("title", message);

        embedDescription = builder
                .comment("Main text of the embed")
                .define("description", description);

        embedThumbnail = builder
                .comment("Thumbnail image URL (small image, top-right corner)")
                .define("thumbnail", "");

        embedColor = builder
                .comment("Color of the embed left border (HEX format, e.g. #ffff00)")
                .define("color", "#ffff00");

        embedImage = builder
                .comment("Large image inside the embed")
                .define("image", "");

        embedFooterIcon = builder
                .comment("Footer icon URL")
                .define("footer_icon", "");

        embedFooter = builder
                .comment("Footer text")
                .define("footer", "Powered by Discord Linker");

        embedOnTimestamp = builder
                .comment("Show current timestamp in embed footer")
                .define("timestamp_enable", true);

        // Используем defineListAllowEmpty вместо устаревшего defineList
        embedFields = builder
                .comment("Additional embed fields. Each string format: \"name|value|inline\"")
                .defineListAllowEmpty("fields", Collections.emptyList(),
                        obj -> obj instanceof String && ((String) obj).split("\\|", 3).length == 3);

        builder.pop(); // завершаем ембед
        builder.pop(); // завершаем событие
    }
}
