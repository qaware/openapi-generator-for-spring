package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiCollectionUtils {

    public static <C extends Collection<T>, T> void setCollectionIfNotEmpty(C collection, Consumer<? super C> setter) {
        if (!collection.isEmpty()) {
            setter.accept(collection);
        }
    }

    public static <T> List<T> emptyListIfNull(@Nullable T[] values) {
        return values == null ? Collections.emptyList() : Arrays.asList(values);
    }

    public static <T> List<T> emptyListIfNull(@Nullable List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
