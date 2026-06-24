package org.hodytrapl.discord_linker.config.general;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.hodytrapl.discord_linker.config.events.EventsConfig;

public class MainConfig {
    // --- Base configuration ---
    public static final MainConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        // Create the builder and pass it to the constructor via a lambda
        Pair<MainConfig, ModConfigSpec> commonPair = new ModConfigSpec.Builder().configure(MainConfig::new);
        INSTANCE = commonPair.getLeft();
        SPEC = commonPair.getRight();
    }

    // --- Configuration parameters (core) ---
    public final ModConfigSpec.ConfigValue<String> langSelect;
    public final ModConfigSpec.BooleanValue enableBot;
    public final ModConfigSpec.ConfigValue<String> botToken;
    public final ModConfigSpec.ConfigValue<String> channelID;
    public final ModConfigSpec.ConfigValue<String> eventsID;
    public final ModConfigSpec.ConfigValue<String> commandsID;

    public final ModConfigSpec.BooleanValue enablePresence;
    public final ModConfigSpec.ConfigValue<String> presenceMessage;

    // Constructor, where the builder passes itself
    public MainConfig(ModConfigSpec.Builder builder){
        /* General settings for the main configuration */
        builder.comment("General settings").push("general");
        langSelect = builder
                .comment("Language selection for the mod. Supported values: [en_us, ru_ru]")
                .define("lang", "en_us");
        enableBot = builder
                .comment("Whether to enable the Discord bot integration. If false, the bot will not start.")
                .define("active", false);

        botToken = builder
                .comment("Your Discord bot token. Obtain it from the Discord Developer Portal under your application's Bot section.")
                .define("bot_token", "BOT_TOKEN_HERE");

        channelID = builder
                .comment("The ID of the Discord text channel where general bot messages (e.g., status updates) will be sent. Must be a valid snowflake ID.")
                .define("channel_id", "0000000000000000000");
        eventsID = builder
                .comment("The ID of the Discord text channel where in-game events (e.g., player join/leave, achievements) will be relayed.")
                .define("events_id", "0000000000000000000");
        commandsID = builder
                .comment("The ID of the Discord text channel where slash-command interactions and responses will be handled.")
                .define("commands_id", "0000000000000000000");

        // Sub-category for Discord presence (Rich Presence)
        builder.comment("Discord Rich Presence settings").push("discord_presence");
        enablePresence = builder
                .comment("Enable or disable Discord Rich Presence. When enabled, the bot will display a custom status message in Discord.")
                .define("show_discord_presence", false);

        presenceMessage = builder
                .comment("The custom message to display in the Discord Rich Presence status (e.g., the server IP or a custom text).")
                .define("message", "play.example.com");
        builder.pop();
        builder.pop();
    }
}