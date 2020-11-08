package de.qaware.openapigeneratorforspring.common.util;

import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiObjectUtils {

    public static <T> void setIf(@Nullable T value, Predicate<T> condition, Consumer<T> setter) {
        if (condition.test(value)) {
            setter.accept(value);
        }
    }

    public static <T> void setIfNotNull(@Nullable T value, Consumer<T> setter) {
        setIf(value, Objects::nonNull, setter);
    }

    public static <T extends HasIsEmpty<?>> void setIfNotEmpty(T value, Consumer<T> setter) {
        setIf(value, v -> !v.isEmpty(), setter);
    }

    public static <T> T ifNullGet(@Nullable T value, Supplier<T> defValueGetter) {
        return value == null ? defValueGetter.get() : value;
    }
}
