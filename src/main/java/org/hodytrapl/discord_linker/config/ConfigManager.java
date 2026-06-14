package org.hodytrapl.discord_linker.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.hodytrapl.discord_linker.config.commands.CommandsConfig;
import org.hodytrapl.discord_linker.config.events.EventsConfig;
import org.hodytrapl.discord_linker.config.mainConfig.MainConfig;

public class ConfigManager {
    public ConfigManager(ModContainer modContainer){
        ModPaths.getConfigDir();
        String modId = modContainer.getModId();
        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                MainConfig.SPEC,
                modId+"/general.toml"
        );
        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                EventsConfig.SPEC,
                modId+"/events.toml"
        );
        CommandsConfig.load();
    }
}
