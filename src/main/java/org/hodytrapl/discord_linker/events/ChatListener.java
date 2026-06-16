package org.hodytrapl.discord_linker.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.hodytrapl.discord_linker.Discord_linker;
import org.hodytrapl.discord_linker.discord.enums.DiscordMessageType;
import org.hodytrapl.discord_linker.utils.config.MainConfigHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hodytrapl.discord_linker.utils.Utils.cleanFormatting;

@EventBusSubscriber(modid = "discord_linker")
public class ChatListener {

    private static final Pattern DISPLAY_NAME_PATTERN =
            Pattern.compile("^\\[(.*?)\\]\\s*(.*?)(?:\\s*\\[(.*?)\\])?$");

    //отправка сообщений майнкрафт в дискорд
    @SubscribeEvent
    public static void receiveMCtoDC(ServerChatEvent event){
        //TODO: сделать проверку на embed
        Player player = event.getPlayer();
        String displayName = player.getDisplayName().getString();
        String username = event.getUsername();
        String message = event.getMessage().getString();

        Matcher matcher = DISPLAY_NAME_PATTERN.matcher(displayName);
        String prefix = "";
        String suffix = "";
        String finalDisplayName;

        if (matcher.matches()) {
            prefix = cleanFormatting(matcher.group(1));
            suffix = cleanFormatting(matcher.group(3) != null ? matcher.group(3) : "");
            finalDisplayName = prefix + username + suffix;
        } else {
            // Если формат не совпал, очищаем всё отображаемое имя
            finalDisplayName = cleanFormatting(displayName);
        }

        String discordMessage = finalDisplayName + ": " + message;
        Discord_linker.getBotManager().sendMessage(
                MainConfigHelper.getRawChannelId(),
                discordMessage,
                DiscordMessageType.CHAT_MESSAGE
        );
    }

    //TODO:сделать отправку из дискорда в чат майнкрафт
}
