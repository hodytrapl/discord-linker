package org.hodytrapl.discord_linker.config.commands;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.mojang.logging.LogUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.hodytrapl.discord_linker.config.ModPaths;
import org.slf4j.Logger;

public class CommandsConfig {
    private static final Path CONFIG_FILE = ModPaths.getConfigDir().resolve("commands.toml");
    private static final Logger LOGGER = LogUtils.getLogger();

    // --- Общие настройки команд (вынесены из general.toml) ---
    private static String commandPrefix = "/";
    private static int normalUserPermissionLevel = 2;
    private static int managementUserPermissionLevel = 4;
    private static List<String> otherBotsPrefixes = new ArrayList<>();

    // --- Список команд ---
    private static List<CommandEntry> commands = new ArrayList<>();

    public static void load() {
        // Файл может ещё не существовать — тогда ничего не читаем, но в MainConfig есть дефолт.
        commands.clear();
        // Сброс настроек к значениям по умолчанию (на случай перезагрузки)
        commandPrefix = "/";
        normalUserPermissionLevel = 2;
        managementUserPermissionLevel = 4;
        otherBotsPrefixes.clear();

        if (!CONFIG_FILE.toFile().exists()) {
            createDefaultConfig();
        }

        // Читаем файл
        try (CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_FILE).build()) {
            config.load();

            // --- Чтение общих настроек команд ---
            String prefix = config.get("command_settings.command_prefix");
            if (prefix != null) commandPrefix = prefix;

            Integer normalLevel = config.get("command_settings.command_normal_user_permission_level");
            if (normalLevel != null) normalUserPermissionLevel = normalLevel;

            Integer managementLevel = config.get("command_settings.command_management_user_permission_level");
            if (managementLevel != null) managementUserPermissionLevel = managementLevel;

            List<String> prefixes = config.get("command_settings.other_bots_command_prefixes");
            if (prefixes != null) otherBotsPrefixes = prefixes;

            // --- Чтение списка команд ---
            var commandsRaw = config.get("command_settings.command");
            if (commandsRaw instanceof List<?> rawList) {
                for (Object obj : rawList) {
                    if (obj instanceof com.electronwill.nightconfig.core.Config tbl) {
                        Boolean enabled = tbl.get("enabled");
                        String minecraftCmd = tbl.get("minecraft_command");
                        String discordCmd = tbl.get("discord_command");
                        Boolean management = tbl.get("management_command");

                        if (minecraftCmd != null && discordCmd != null && management != null && enabled != null) {
                            commands.add(new CommandEntry(
                                    enabled, minecraftCmd, discordCmd, management
                            ));
                        } else {
                            LOGGER.warn("Skipping incomplete command entry: {}", tbl);
                        }
                    }
                }
            }

            LOGGER.info("Loaded {} commands from config", commands.size());
        } catch (Exception e) {
            LOGGER.error("Failed to load commands from config", e);
        }
    }

    public static class CommandEntry {
        public final boolean enabled;
        public final String minecraftCommand;
        public final String discordCommand;
        public final boolean managementCommand;

        public CommandEntry( boolean enabled,String minecraftCommand, String discordCommand,
                            boolean managementCommand) {
            this.enabled = enabled;
            this.minecraftCommand = minecraftCommand;
            this.discordCommand = discordCommand;
            this.managementCommand = managementCommand;
        }
    }

    private static String generateDefaultTemplate() {
        // Используем StringBuilder для удобства добавления новых секций
        StringBuilder sb = new StringBuilder();

        sb.append("# Configuration for Minecraft <-> Discord command mapping\n");
        sb.append("# This file is auto-generated if missing. Edit it to add your commands.\n\n");

        sb.append("[command_settings]\n");
        sb.append("\t#-\n");
        sb.append("\tcommand_prefix = \"").append(commandPrefix).append("\"\n");
        sb.append("\t#-\n");
        sb.append("\t# Range: 0 ~ 4\n");
        sb.append("\tcommand_normal_user_permission_level = ").append(normalUserPermissionLevel).append("\n");
        sb.append("\t#-\n");
        sb.append("\tcommand_management_user_permission_level = ").append(managementUserPermissionLevel).append("\n");
        sb.append("\t#-\n");
        sb.append("\tother_bots_command_prefixes = []\n\n");

        // Пример команды (можно сделать массив примеров и циклом)
        sb.append("\t#Example command (add more blocks like this)\n");
        sb.append("\t[[command_settings.command]]\n");
        sb.append("\t\t#-\n");
        sb.append("\t\tenabled = true\n");
        sb.append("\t\t#-\n");
        sb.append("\t\tminecraft_command = \"neoforge tps\"\n");
        sb.append("\t\t#-\n");
        sb.append("\t\tdiscord_command = \"tps\"\n");
        sb.append("\t\t#-\n");
        sb.append("\t\tmanagement_command = false\n");


        return sb.toString();
    }

    private static void createDefaultConfig() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            Files.writeString(CONFIG_FILE, generateDefaultTemplate());
            LOGGER.info("Created default commands.toml with template. Edit it and restart/reload.");
        } catch (Exception e) {
            LOGGER.error("Failed to create default commands.toml", e);
        }
    }
    // --- Геттеры для доступа из других частей мода ---
    public static String getCommandPrefix() {
        return commandPrefix;
    }

    public static int getNormalUserPermissionLevel() {
        return normalUserPermissionLevel;
    }

    public static int getManagementUserPermissionLevel() {
        return managementUserPermissionLevel;
    }

    public static List<String> getOtherBotsPrefixes() {
        return otherBotsPrefixes;
    }

    public static List<CommandEntry> getCommands() {
        return commands;
    }
}