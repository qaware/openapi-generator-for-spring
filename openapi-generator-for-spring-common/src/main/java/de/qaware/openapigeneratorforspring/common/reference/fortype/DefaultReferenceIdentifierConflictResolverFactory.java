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

package de.qaware.openapigeneratorforspring.common.reference.fortype;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultReferenceIdentifierConflictResolverFactory {

    public <T, R extends ReferenceIdentifierConflictResolverForType<T>> R create(Function<ReferenceIdentifierConflictResolverForType<T>, R> mapper) {
        return mapper.apply((itemsWithSameReferenceIdentifier, originalIdentifier) ->
                IntStream.range(0, itemsWithSameReferenceIdentifier.size()).boxed()
                        .map(i -> originalIdentifier + "_" + i)
                        .collect(Collectors.toList())
        );
    }
}
