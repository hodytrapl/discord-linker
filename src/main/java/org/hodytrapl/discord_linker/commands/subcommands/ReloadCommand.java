package org.hodytrapl.discord_linker.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.hodytrapl.discord_linker.Discord_linker;

public class ReloadCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("reload")
                .requires(source -> source.hasPermission(4)) // только операторы
                .executes(context -> {
                    // Перезапускаем бота
                    Discord_linker.getBotManager().reloadBot();
                    context.getSource().sendSuccess(() -> Component.literal("Discord bot reloaded"), true);
                    return 1;
                });
    }
}