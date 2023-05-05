package com.shimao.iot.core;

import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Add some description about this class.
 *
 * @author striver.cradle
 */
public class NamingStrategy {

    public static final NamingStrategy SAME_CASE = new NamingStrategy();

    public static final NamingStrategy SNAKE_CASE = new SnakeCaseStrategy();

    @NonNull
    public String translate(String name) {
        Objects.requireNonNull(name);
        return name;
    }

    private static class SnakeCaseStrategy extends NamingStrategy {
        @Override
        public String translate(String name) {
            Objects.requireNonNull(name);
            int length = name.length();
            StringBuilder result = new StringBuilder(length * 2);
            int resultLength = 0;
            boolean wasPrevTranslated = false;
            for (int i = 0; i < length; i++) {
                char c = name.charAt(i);
                if (i > 0 || c != '_') // skip first starting underscore
                {
                    if (Character.isUpperCase(c)) {
                        if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                            result.append('_');
                            resultLength++;
                        }
                        c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                    } else {
                        wasPrevTranslated = false;
                    }
                    result.append(c);
                    resultLength++;
                }
            }
            return resultLength > 0 ? result.toString() : name;
        }
    }
}
