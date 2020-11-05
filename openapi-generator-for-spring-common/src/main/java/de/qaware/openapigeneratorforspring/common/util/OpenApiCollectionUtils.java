package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiCollectionUtils {

    public static <T, U> Optional<U> firstNonNull(List<T> items, Function<T, U> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .findFirst();
    }

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
