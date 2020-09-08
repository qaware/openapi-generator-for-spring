package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiObjectUtils {

    public static <T> boolean setIfNotNull(@Nullable T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
            return true;
        }
        return false;
    }

}
