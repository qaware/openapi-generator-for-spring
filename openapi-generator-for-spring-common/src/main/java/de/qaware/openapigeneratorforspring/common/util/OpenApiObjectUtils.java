package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiObjectUtils {

    public static <T> boolean setIf(@Nullable T value, Predicate<T> condition, Consumer<T> setter) {
        if (condition.test(value)) {
            setter.accept(value);
            return true;
        }
        return false;
    }

    public static <T> boolean setIfNotNull(@Nullable T value, Consumer<T> setter) {
        return setIf(value, Objects::nonNull, setter);
    }

}
