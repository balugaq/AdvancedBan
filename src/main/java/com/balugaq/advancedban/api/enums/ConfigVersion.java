package com.balugaq.advancedban.api.enums;

import org.jetbrains.annotations.NotNull;

public enum ConfigVersion {
    C_UNKNOWN,
    C_20250221_1,
    C_20250221_2,
    C_20250222_1,
    ;

    ConfigVersion() {

    }

    public static boolean isBefore(@NotNull ConfigVersion version1, @NotNull ConfigVersion version2) {
        return version1.ordinal() < version2.ordinal();
    }

    public static boolean isAfter(@NotNull ConfigVersion version1, @NotNull ConfigVersion version2) {
        return version1.ordinal() > version2.ordinal();
    }

    public static boolean isAtLeast(@NotNull ConfigVersion version1, @NotNull ConfigVersion version2) {
        return version1.ordinal() >= version2.ordinal();
    }

    public static boolean isAtMost(@NotNull ConfigVersion version1, @NotNull ConfigVersion version2) {
        return version1.ordinal() <= version2.ordinal();
    }
}
