package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiMapUtils {

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

    public static <T, V> Map<String, V> buildStringMapFromStream(
            Stream<T> stream,
            Function<? super T, String> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return buildMapFromStream(stream, ensureKeyIsNotBlank(keyMapper), valueMapper);
    }

    private static <T, K, V> Map<K, V> buildMapFromStream(
            Stream<T> stream,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        return buildMapFromStream(stream, keyMapper, valueMapper, LinkedHashMap::new);
    }

    public static <T, V, M extends Map<String, V>> M buildStringMapFromStream(
            Stream<T> stream,
            Function<? super T, String> keyMapper,
            Function<? super T, ? extends V> valueMapper,
            Supplier<? extends M> mapSupplier
    ) {
        return buildMapFromStream(stream,
                ensureKeyIsNotBlank(keyMapper),
                valueMapper,
                mapSupplier
        );
    }

    private static <T, K, V, M extends Map<K, V>> M buildMapFromStream(
            Stream<T> stream,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper,
            Supplier<? extends M> mapSupplier
    ) {
        // TODO handle possible duplicate entries here?
        return stream.collect(Collectors.toMap(
                keyMapper,
                valueMapper,
                (a, b) -> {
                    throw new IllegalStateException("Duplicate key for values " + a + " vs. " + b);
                },
                mapSupplier
        ));
    }

    public static <T> Function<T, String> ensureKeyIsNotBlank(Function<? super T, String> keyMapper) {
        return value -> {
            String key = keyMapper.apply(value);
            if (StringUtils.isBlank(key)) {
                throw new IllegalArgumentException("Key must not be blank");
            }
            return key;
        };
    }
}
