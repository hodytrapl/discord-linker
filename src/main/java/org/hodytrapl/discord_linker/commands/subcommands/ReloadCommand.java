    package org.hodytrapl.discord_linker.commands.subcommands;

    import com.mojang.brigadier.builder.LiteralArgumentBuilder;
    import net.minecraft.commands.CommandSourceStack;
    import net.minecraft.commands.Commands;
    import net.minecraft.network.chat.Component;
    import org.hodytrapl.discord_linker.Discord_linker;

    import static org.hodytrapl.discord_linker.LanguageManager.getMessage;

    public class ReloadCommand {
        //регистрируем команду
        public static LiteralArgumentBuilder<CommandSourceStack> register() {
            return Commands.literal("reload")
                    .requires(source -> source.hasPermission(4)) // только операторы
                    .executes(context -> {
                        // Перезапускаем бота
                        Discord_linker.getBotManager().reloadBot();
                        context.getSource().sendSuccess(() -> Component.literal(getMessage("mod.typeminecraft.commands.subcommands.reloadcommand.botreloadsuccess")+"\n"), true);
                        return 1;
                    });
        }
    }