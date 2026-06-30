package org.hodytrapl.discordvoxelsvoices.events;

import com.mojang.logging.LogUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.hodytrapl.discordvoxelsvoices.Discordvoxelsvoices;
import org.hodytrapl.discordvoxelsvoices.config.events.EventsConfig;
import org.hodytrapl.discordvoxelsvoices.discord.DiscordBotManager;
import org.hodytrapl.discordvoxelsvoices.discord.GeneratorEmbedMessage;
import org.hodytrapl.discordvoxelsvoices.discord.enums.DiscordMessageType;
import org.hodytrapl.discordvoxelsvoices.utils.ValidationUtils;
import org.hodytrapl.discordvoxelsvoices.utils.config.EventsConfigHelper;
import org.hodytrapl.discordvoxelsvoices.utils.config.MainConfigHelper;
import org.slf4j.Logger;

import java.util.Map;

import static org.hodytrapl.discordvoxelsvoices.utils.Utils.formatPlaceholder;

/**
 * Слушатель событий входа и выхода игроков на сервер.
 * <p>
 * Отправляет уведомления в Discord при подключении или отключении игрока,
 * используя конфигурацию событий {@code playerJoin} и {@code playerLeave}.
 * </p>
 */
@EventBusSubscriber(modid = "discordvoxelsvoices")
public class JoinLeavePlayerListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Обрабатывает событие входа игрока на сервер.
     *
     * @param event событие входа игрока
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // проверка на срабатывания ивента
        if (!EventsConfigHelper.isEventEnabled(EventsConfig.INSTANCE.playerJoin)) return;

        //форматируем сообщение
        Player player = event.getEntity();
        String username = player.getName().getString();
        String message = EventsConfigHelper.getFormattedEventMessage(EventsConfig.INSTANCE.playerJoin,"username",username);

        //находим бота
        DiscordBotManager botManager = Discordvoxelsvoices.getBotManager();
        String channelId = MainConfigHelper.getRawChannelId();
        String eventId = MainConfigHelper.getRawEventsId();
        String correctId=eventId;
        if(!ValidationUtils.isValidId(eventId)){
            if(!ValidationUtils.isValidId(channelId)){
                return;
            }
            correctId=channelId;
        }

        //даем запрос
        if (ValidationUtils.isValidId(correctId)) {
            // проверка embed включен
            if (EventsConfigHelper.isEmbedEnable(EventsConfig.INSTANCE.playerJoin)) {
                MessageEmbed embed = GeneratorEmbedMessage.buildEmbed(EventsConfig.INSTANCE.playerJoin, username);
                botManager.sendEmbed(correctId, embed);
            } else {
                botManager.sendMessage(correctId, message, DiscordMessageType.CHAT_MESSAGE);
            }
        }
    }

    /**
     * Обрабатывает событие выхода игрока с сервера.
     *
     * @param event событие выхода игрока
     */
    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        // проверка на срабатывания ивента
        if (!EventsConfigHelper.isEventEnabled(EventsConfig.INSTANCE.playerLeave)) return;

        //форматируем сообщение
        Player player = event.getEntity();
        String username = player.getName().getString();
        String message = EventsConfigHelper.getFormattedEventMessage(EventsConfig.INSTANCE.playerLeave,"username",username);

        //находим бота
        DiscordBotManager botManager = Discordvoxelsvoices.getBotManager();
        String channelId = MainConfigHelper.getRawChannelId();
        String eventId = MainConfigHelper.getRawEventsId();
        String correctId=eventId;
        if(!ValidationUtils.isValidId(eventId)){
            if(!ValidationUtils.isValidId(channelId)){
                return;
            }
            correctId=channelId;
        }

        //даем запрос
        if (ValidationUtils.isValidId(correctId)) {
            // проверка embed включен
            if (EventsConfigHelper.isEmbedEnable(EventsConfig.INSTANCE.playerLeave)) {
                MessageEmbed embed = GeneratorEmbedMessage.buildEmbed(EventsConfig.INSTANCE.playerLeave, username);
                botManager.sendEmbed(correctId, embed);
            } else {
                botManager.sendMessage(correctId, message, DiscordMessageType.CHAT_MESSAGE);
            }
        }
    }
}