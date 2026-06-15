package org.hodytrapl.discord_linker.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.hodytrapl.discord_linker.Discord_linker;
import org.hodytrapl.discord_linker.config.commands.CommandsConfig;
import org.hodytrapl.discord_linker.config.general.MainConfig;
import org.hodytrapl.discord_linker.discord.DiscordBotManager;
import org.hodytrapl.discord_linker.discord.enums.DiscordMessageType;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import static org.hodytrapl.discord_linker.utils.ValidationUtils.isValidId;

public class CommandListener extends ListenerAdapter {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String commandPrefix;
    private final java.util.List<? extends String> otherPrefixes;
    private final String allowedChannelId;

    public CommandListener() {
        this.commandPrefix = CommandsConfig.INSTANCE.getCommandPrefix();
        this.otherPrefixes = CommandsConfig.INSTANCE.getOtherBotsPrefixes();

        String commandsId = MainConfig.INSTANCE.commandsID.get();
        String channelId = MainConfig.INSTANCE.channelID.get();
        String resolvedId;

        if (isValidId(commandsId)) {
            resolvedId = commandsId;
            LOGGER.info("Using commands channel: {}", resolvedId);
        } else if (isValidId(channelId)) {
            resolvedId = channelId;
            LOGGER.info("commandsID not set, falling back to channelID: {}", resolvedId);
        } else {
            LOGGER.warn("Neither commandsID nor channelID is configured. Commands will be ignored.");
            resolvedId = "DISABLED";
        }

        this.allowedChannelId = resolvedId;
        if (allowedChannelId.equals("DISABLED")) {
            LOGGER.info("Discord commands are disabled because no channel ID is set.");
        } else {
            LOGGER.info("Commands will only be accepted in channel ID: {}", allowedChannelId);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (allowedChannelId.equals("DISABLED")) return;
        if (!event.getChannel().getId().equals(allowedChannelId)) return;

        String rawMessage = event.getMessage().getContentRaw();
        if (!rawMessage.startsWith(commandPrefix)) return;

        for (String otherPrefix : otherPrefixes) {
            if (rawMessage.startsWith(commandPrefix + otherPrefix)) {
                return;
            }
        }

        String withoutPrefix = rawMessage.substring(commandPrefix.length());
        String[] parts = withoutPrefix.split("\\s+");
        if (parts.length == 0) return;

        String command = parts[0].toLowerCase();

        event.getChannel().sendMessage("Выполняю команду...").queue(msg -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                msg.editMessage("Ошибка: сервер недоступен").queue();
                return;
            }

            server.execute(() -> {
                String output = switch (command) {
                    case "tps" -> MinecraftCommandExecutor.executeCommandWithOutput(
                            CommandsConfig.INSTANCE.getTPSCommand().minecraftCommand());
                    case "modlist" -> MinecraftCommandExecutor.executeCommandWithOutput(
                            CommandsConfig.INSTANCE.getModListCommand().minecraftCommand());
                    case "list" -> MinecraftCommandExecutor.executeCommandWithOutput(
                            CommandsConfig.INSTANCE.getOnlineListCommand().minecraftCommand());
                    default -> "Неизвестная команда. Доступно: tps, modlist, list.";
                };

                DiscordBotManager botManager = Discord_linker.getBotManager();

                botManager.sendMessage(event.getChannel().getId(), output, DiscordMessageType.COMMAND_RESPONSE);

                msg.delete().queue();
            });
        });
    }
}
