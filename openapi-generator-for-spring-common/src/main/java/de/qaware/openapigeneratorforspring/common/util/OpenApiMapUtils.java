package de.qaware.openapigeneratorforspring.common.util;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenApiMapUtils {

    private OpenApiMapUtils() {
        // static methods only
    }

    public static <M extends Map<K, V>, K, V> void mergeWithExistingMap(Supplier<? extends M> getter, Consumer<? super M> setter, M toBeMerged) {
        M existingMap = getter.get();
        if (existingMap != null) {
            existingMap.putAll(toBeMerged);
        } else {
            setMapIfNotEmpty(setter, toBeMerged);
        }
    }

    public static <M extends Map<K, V>, K, V> void setMapIfNotEmpty(Consumer<? super M> setter, M map) {
        if (!map.isEmpty()) {
            setter.accept(map);
        }
    }

    public static <T, K, V> Map<K, V> buildMapFromArray(
            T[] array,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        // TODO handle possible duplicate entries here?
        return Stream.of(array).collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
