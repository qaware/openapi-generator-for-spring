/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static <L, R, T> Stream<T> zip(Stream<L> leftStream, Stream<R> rightStream, BiFunction<L, R, T> combiner) {
        Spliterator<L> lefts = leftStream.spliterator();
        Spliterator<R> rights = rightStream.spliterator();
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.min(lefts.estimateSize(), rights.estimateSize()), lefts.characteristics() & rights.characteristics()) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return lefts.tryAdvance(left -> rights.tryAdvance(right -> action.accept(combiner.apply(left, right))));
            }
        }, false);
    }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
        Spliterator<T> spliterator = stream.spliterator();
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(spliterator.estimateSize(), spliterator.characteristics()) {
            boolean stillGoing = true;

            @Override
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (stillGoing) {
                    return spliterator.tryAdvance(elem -> {
                        if (predicate.test(elem)) {
                            consumer.accept(elem);
                        } else {
                            stillGoing = false;
                        }
                    });
                }
                return false;
            }
        }, false);
    }
}
