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
