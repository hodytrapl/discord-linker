package org.hodytrapl.discord_linker.config;

import net.neoforged.fml.loading.FMLPaths;
import org.hodytrapl.discord_linker.Discord_linker;

import java.nio.file.Path;

public class ModPaths {
    //храним гдето в своем файле, пример с плагинов
    private static final String MOD_DIR_NAME = Discord_linker.MODID;
    private static Path configDir = null;

    public static Path getConfigDir() {
        if (configDir == null) {
            configDir = FMLPaths.CONFIGDIR.get().resolve(MOD_DIR_NAME);
            if (!configDir.toFile().exists()) {
                configDir.toFile().mkdirs();
            }
        }
        return configDir;
    }
}