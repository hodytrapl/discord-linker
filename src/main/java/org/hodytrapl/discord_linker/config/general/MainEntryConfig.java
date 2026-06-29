package org.hodytrapl.discord_linker.config.general;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;

public class MainEntryConfig {
    // Main event settings
    public final ModConfigSpec.BooleanValue enable;
    public final ModConfigSpec.ConfigValue<String> channelId;
    public final ModConfigSpec.ConfigValue<Integer> updateInterval;
    public final ModConfigSpec.ConfigValue<String> name_channel;

    public MainEntryConfig(ModConfigSpec.Builder builder, String eventName, Map<String, String> payload) {
        builder.comment("Configuration for " + eventName).push(eventName);

        // Enable/disable the event
        // If "enable" parameter is not specified in payload, event is enabled by default (true)
        boolean defaultEnable = Boolean.parseBoolean(payload.getOrDefault("enable", "true"));
        enable = builder
                .comment("Enable or disable this event")
                .define("enable", defaultEnable);

        // Discord channel ID
        channelId = builder
                .comment("The ID of the Discord voice channel (e.g., status updates) will be sent. Must be a valid snowflake ID.")
                .define("channel_id", payload.getOrDefault("channel_id", "0000000000000000000"));

        // Update interval
        int defaultUpdateInterval = Integer.parseInt(payload.getOrDefault("update_interval", "30"));
        updateInterval = builder
                .comment("Update interval for channel name in seconds",
                        "Value -1: updates only once during bot initialization",
                        "Value 0: disables automatic updates completely",
                        "Positive value: periodic updates at the specified interval",
                        "Recommended value: 30-60 seconds for balance between freshness and performance",
                        "Examples: -1 (initialization only), 30 (every 30 seconds), 60 (every minute)")
                .define("update_interval", defaultUpdateInterval);

        // Channel name template
        name_channel = builder
                .comment("Channel name template with placeholder support",
                        "Supported variables depend on the event type",
                        "Usage examples:",
                        "  - \"Staff Players: %onlinestaff%/%maxstaff%\" - shows staff online count",
                        "  - \"Version: %version_server%\" - displays server version",
                        "  - \"Players: %online%/%max%\" - shows current player count",
                        "If the template is empty (\"\"), the bot will not change the channel name",
                        "When used with update_interval = -1, the name is set only once during startup")
                .define("name_channel", payload.getOrDefault("name_channel", ""));

        builder.pop(); // end event configuration
    }
}