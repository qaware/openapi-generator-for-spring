package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiEnumUtils {

    public static <T> T findEnumByToString(Object object, Class<T> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(t -> t.toString().equals(object.toString()))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one enum in " + enumClass.getSimpleName()
                            + " for '" + object + "': " + a + " vs. " + b);
                })
                .orElseThrow(() -> new IllegalStateException("Cannot find enum in " + enumClass.getSimpleName() + " for '" + object + "'"));
    }

}
