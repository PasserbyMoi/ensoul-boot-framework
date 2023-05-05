package com.shimao.iot.core.sort;

import java.util.Locale;

/**
 * @author striver.cradle
 */
public enum Direction {

    ASC,
    DESC;

    /**
     * Returns the {@link Direction} enum for the given {@link String} value.
     *
     * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
     */
    public static Direction fromString(String value) {
        try {
            return Direction.valueOf(value.toUpperCase(Locale.US));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
        }
    }

    /**
     * Returns the {@link Direction} enum for the given {@link String} or null if it cannot be parsed into an enum
     * value.
     */
    public static Direction fromStringOrNull(String value) {
        try {
            return fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
