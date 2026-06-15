package org.hodytrapl.discord_linker.utils.config;

import org.hodytrapl.discord_linker.config.commands.CommandsConfig;
import java.util.List;

public class CommandsConfigHelper {

    public static String getCommandPrefix() {
        return CommandsConfig.INSTANCE.getCommandPrefix();
    }

    public static int getNormalUserPermissionLevel() {
        return CommandsConfig.INSTANCE.getNormalUserPermissionLevel();
    }

    public static int getManagementUserPermissionLevel() {
        return CommandsConfig.INSTANCE.getManagementUserPermissionLevel();
    }

    public static List<? extends String> getOtherBotsPrefixes() {
        return CommandsConfig.INSTANCE.getOtherBotsPrefixes();
    }

    public static String getTpsMinecraftCommand() {
        return CommandsConfig.INSTANCE.getTPSCommand().minecraftCommand();
    }

    public static String getModListMinecraftCommand() {
        return CommandsConfig.INSTANCE.getModListCommand().minecraftCommand();
    }

    public static String getOnlineListMinecraftCommand() {
        return CommandsConfig.INSTANCE.getOnlineListCommand().minecraftCommand();
    }

    public static boolean isTpsCommandEnabled() {
        return CommandsConfig.INSTANCE.getTPSCommand().enabled.get();
    }

    public static boolean isModListCommandEnabled() {
        return CommandsConfig.INSTANCE.getModListCommand().enabled.get();
    }

    public static boolean isOnlineListCommandEnabled() {
        return CommandsConfig.INSTANCE.getOnlineListCommand().enabled.get();
    }
}