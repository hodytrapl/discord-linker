package org.hodytrapl.discord_linker.utils;

public class ValidationUtils {
    private ValidationUtils() {} // static utility

    //валидируем айди
    public static boolean isValidId(String id) {
        return id != null && !id.isEmpty() && !id.equals("0000000000000000000") && !id.equals("BOT_TOKEN_HERE");
    }
}