package org.hodytrapl.discord_linker.config;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.hodytrapl.discord_linker.Discord_linker;
import org.hodytrapl.discord_linker.LanguageManager;
import org.slf4j.Logger;

@EventBusSubscriber(modid = "discord_linker")
public class ConfigReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Первая загрузка конфига (серверный тип)
    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            LOGGER.info("Loading Discord Linker configuration...");
            reloadEverything();
        }
    }

    // Перезагрузка конфига (по /reload или изменению файла)
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            LOGGER.info("Reloading Discord Linker configuration...");
            reloadEverything();
        }
    }

    private static void reloadEverything() {
        try {
            LanguageManager.reload(); // Теперь конфиг загружен, можно читать langSelect
            Discord_linker.getBotManager().reloadBot();
            LOGGER.info("Reload completed.");
        } catch (Exception e) {
            LOGGER.error("Failed to reload", e);
        }
    }
}