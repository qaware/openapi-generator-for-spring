package de.qaware.openapigeneratorforspring.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OpenApiStringUtils {

    private OpenApiStringUtils() {
        // static methods only
    }

    public static boolean setStringIfNotBlank(@Nullable String value, Consumer<String> setter) {
        if (StringUtils.isNotBlank(value)) {
            setter.accept(value);
            return true;
        }
        return false;
    }

}
