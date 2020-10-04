package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiStringUtils {

    public static boolean setStringIfNotBlank(@Nullable String value, Consumer<String> setter) {
        return OpenApiObjectUtils.setIf(value, StringUtils::isNotBlank, setter);
    }

    public static void setStringRequireNonBlank(@Nullable String value, Consumer<String> setter) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException("String value '" + value + "'  must not be blank");
        }
        setter.accept(value);
    }
}
