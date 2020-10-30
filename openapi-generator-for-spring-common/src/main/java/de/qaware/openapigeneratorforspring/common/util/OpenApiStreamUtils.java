package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiStreamUtils {

    public static <K, V>
    Collector<Pair<K, V>, ?, Map<K, List<V>>> groupingByPairKeyAndCollectingValuesToList() {
        return groupingByPairKeyAndCollectingValuesTo(toList());
    }

    public static <K, V, A, D>
    Collector<Pair<K, V>, ?, Map<K, D>> groupingByPairKeyAndCollectingValuesTo(Collector<? super V, A, D> downstream) {
        return groupingByWithMappingTo(Pair::getKey, Pair::getValue, downstream);
    }

    @SuppressWarnings("squid:S1452") // suppress generic wild card usage
    public static <T, U, K, A, D>
    Collector<T, ?, Map<K, D>> groupingByWithMappingTo(
            Function<? super T, ? extends K> classifier,
            Function<? super T, ? extends U> downstreamMapper,
            Collector<? super U, A, D> downstream
    ) {
        return groupingBy(classifier, LinkedHashMap::new, mapping(downstreamMapper, downstream));
    }
}
