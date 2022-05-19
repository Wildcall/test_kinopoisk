package ru.malygin.parser.context.util;

import javax.annotation.Nullable;

public abstract class Assert {

    public static void notNull(@Nullable Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notNull(@Nullable Object object,
                               String msg) {
        if (object == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
