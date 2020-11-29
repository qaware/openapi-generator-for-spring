/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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


import java.util.List;

/**
 * Conflict resolver for non-unique identifiers returned by {@link ReferenceIdentifierBuilderForType}.
 *
 * @param <T> type of the to be referenced item
 */
@FunctionalInterface
public interface ReferenceIdentifierConflictResolverForType<T> {
    /**
     * From the list of given items with same identifier, builds
     * a list of same length containing unique identifiers.
     *
     * <p>Default implementation just adds indexes as suffix to each item.
     *
     * @param itemsWithSameReferenceIdentifier conflicting items with same identifier
     * @param originalIdentifier               original shared identifier for all items
     * @return list of unique identifiers
     */
    List<String> resolveConflict(List<T> itemsWithSameReferenceIdentifier, String originalIdentifier);
}
