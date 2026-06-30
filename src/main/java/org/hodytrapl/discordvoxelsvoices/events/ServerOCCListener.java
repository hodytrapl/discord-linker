package org.hodytrapl.discordvoxelsvoices.events;

import com.mojang.logging.LogUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.*;
import org.hodytrapl.discordvoxelsvoices.Discordvoxelsvoices;
import org.hodytrapl.discordvoxelsvoices.config.events.EventEntryConfig;
import org.hodytrapl.discordvoxelsvoices.config.events.EventsConfig;
import org.hodytrapl.discordvoxelsvoices.discord.DiscordBotManager;
import org.hodytrapl.discordvoxelsvoices.discord.GeneratorEmbedMessage;
import org.hodytrapl.discordvoxelsvoices.discord.enums.DiscordMessageType;
import org.hodytrapl.discordvoxelsvoices.utils.ValidationUtils;
import org.hodytrapl.discordvoxelsvoices.utils.config.EventsConfigHelper;
import org.hodytrapl.discordvoxelsvoices.utils.config.MainConfigHelper;
import org.slf4j.Logger;

import static org.hodytrapl.discordvoxelsvoices.LanguageManager.getMessage;

/**
 * Слушатель событий жизненного цикла сервера (запуск, остановка, краш).
 * <p>
 * Отправляет уведомления в Discord о старте, штатной остановке или
 * аварийном завершении сервера. При краше используется синхронная
 * отправка, чтобы сообщение гарантированно ушло перед закрытием бота.
 * </p>
 */
@EventBusSubscriber(modid = "discordvoxelsvoices")
public class ServerOCCListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean isGracefulShutdown = false;

    /**
     * Обрабатывает событие запуска сервера.
     *
     * @param event событие запуска сервера
     */
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        isGracefulShutdown = false; // сброс при старте
        LOGGER.info(getMessage("mod.typelogger.events.server.starting"));
        Discordvoxelsvoices.setServer(server);
        Discordvoxelsvoices.getBotManager().initializeBot(server);
        // Для старта можно оставить асинхронную отправку (сервер ещё работает)
        sendEventMessage(EventsConfig.INSTANCE.serverStarted, false);
    }

    /**
     * Обрабатывает событие начала остановки сервера.
     * <p>
     * Устанавливает флаг корректной остановки, чтобы при {@code ServerStoppedEvent}
     * можно было отличить штатное завершение от краша.
     * </p>
     *
     * @param event событие остановки сервера
     */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        isGracefulShutdown = true; // нормальная остановка
    }

    /**
     * Обрабатывает событие полной остановки сервера.
     * <p>
     * Отправляет сообщение о краше или штатной остановке в зависимости
     * от флага {@code isGracefulShutdown}, затем завершает работу бота.
     * </p>
     *
     * @param event событие остановки сервера
     */
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        boolean crashed = !isGracefulShutdown;
        if (crashed) {
            LOGGER.error(getMessage("mod.typelogger.events.server.crashed"));
            sendEventMessage(EventsConfig.INSTANCE.serverCrashed, true); // синхронно
        } else {
            LOGGER.info(getMessage("mod.typelogger.events.server.stopped"));
            sendEventMessage(EventsConfig.INSTANCE.serverStopped, true); // синхронно
        }
        // Закрываем бота после отправки
        Discordvoxelsvoices.getBotManager().shutdown();
    }

    /**
     * Отправляет сообщение о событии сервера в Discord.
     *
     * @param eventConfig конфигурация события
     * @param sync флаг синхронной отправки (блокирующий поток)
     */
    private static void sendEventMessage(EventEntryConfig eventConfig, boolean sync) {
        if (!EventsConfigHelper.isEventEnabled(eventConfig)) return;
        String message = EventsConfigHelper.getRawEventMessage(eventConfig);
        DiscordBotManager botManager = Discordvoxelsvoices.getBotManager();
        if (botManager == null) return;

        String channelId = MainConfigHelper.getRawChannelId();
        String eventId = MainConfigHelper.getRawEventsId();
        String correctId = ValidationUtils.isValidId(eventId) ? eventId :
                (ValidationUtils.isValidId(channelId) ? channelId : null);
        if (correctId == null) return;

        if (EventsConfigHelper.isEmbedEnable(eventConfig)) {
            MessageEmbed embed = GeneratorEmbedMessage.buildEmbed(eventConfig, "");
            if (sync) {
                botManager.sendEmbedSync(correctId, embed);
            } else {
                botManager.sendEmbed(correctId, embed);
            }
        } else {
            if (sync) {
                botManager.sendMessageSync(correctId, message, DiscordMessageType.CHAT_MESSAGE);
            } else {
                botManager.sendMessage(correctId, message, DiscordMessageType.CHAT_MESSAGE);
            }
        }
    }
}