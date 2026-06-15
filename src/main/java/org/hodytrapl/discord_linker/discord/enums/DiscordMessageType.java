package org.hodytrapl.discord_linker.discord.enums;

public enum DiscordMessageType {
    /** Response to a user command – wrapped in a code block */
    COMMAND_RESPONSE,
    /** A plain chat message (e.g., from Minecraft player to Discord) */
    CHAT_MESSAGE,
    /** System info/warning/error – prefixed with [System] */
    SYSTEM_NOTIFICATION
}