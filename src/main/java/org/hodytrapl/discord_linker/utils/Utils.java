package org.hodytrapl.discord_linker.utils;

import net.minecraft.ChatFormatting;

public class Utils {
    public static String cleanFormatting(String text) {
        if (text == null || text.isEmpty()) return "";

        // 1. Приводим все & к § для единообразной обработки
        String withSection = text.replace('&', '§');

        // 2. Удаляем все коды форматирования (§ + следующий символ)
        String stripped = ChatFormatting.stripFormatting(withSection);

        // 3. Если после удаления остались символы §, значит они не были частью кодов
        //    (например, обычный текст), и их нужно вернуть в & (так как изначально они были &)
        return stripped.replace('§', '&');
    }
}
