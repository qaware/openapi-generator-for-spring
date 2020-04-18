package de.qaware.openapigeneratorforspring.common.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OpenApiMapUtils {

    private OpenApiMapUtils() {
        // static methods only
    }

    public static <M extends Map<K, V>, K, V> void mergeWithExistingMap(Supplier<? extends M> getter, Consumer<? super M> setter, M toBeMerged) {
        M existingMap = getter.get();
        if (existingMap != null) {
            existingMap.putAll(toBeMerged);
        } else {
            setMapIfNotEmpty(toBeMerged, setter);
        }
    }

    public static <M extends Map<K, V>, K, V> boolean setMapIfNotEmpty(M map, Consumer<? super M> setter) {
        if (!map.isEmpty()) {
            setter.accept(map);
            return true;
        }
        return false;
    }

    public static <T, K, V> Map<K, V> buildMapFromArray(
            T[] array,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return buildMapFromArray(array, keyMapper, valueMapper, LinkedHashMap::new);
    }

    public static <T, K, V, M extends Map<K, V>> M buildMapFromArray(
            T[] array,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper,
            Supplier<? extends M> mapSupplier
    ) {
        // TODO handle possible duplicate entries here?
        return Arrays.stream(array).collect(Collectors.toMap(
                keyMapper,
                valueMapper,
                (a, b) -> {
                    throw new IllegalStateException("Duplicate key " + a);
                },
                mapSupplier
        ));
    }
}
