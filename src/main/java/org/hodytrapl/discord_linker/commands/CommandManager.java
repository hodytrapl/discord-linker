package org.hodytrapl.discord_linker.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.hodytrapl.discord_linker.commands.subcommands.ModListCommand;
import org.hodytrapl.discord_linker.commands.subcommands.ReloadCommand;

public class CommandManager {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Создаем корень команды, discordlinker
        LiteralArgumentBuilder<CommandSourceStack> rootCommand = Commands.literal("discordlinker");

        // Подключаем логику подкоманд
        rootCommand.then(ModListCommand.register());
        rootCommand.then(ReloadCommand.register());

        // Регистрируем всю конструкцию в игре
        dispatcher.register(rootCommand);
    }
}
