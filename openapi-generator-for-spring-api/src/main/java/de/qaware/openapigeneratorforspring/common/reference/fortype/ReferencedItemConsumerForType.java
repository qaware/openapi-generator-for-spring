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

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;

import java.util.function.Consumer;

/**
 * Interface for consuming to be referenced
 * items during Open Api model building.
 *
 * <p>Specific instances can be obtained via the {@link
 * de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier
 * supplier}.
 *
 * <p>By default, items are only "maybe" referenced. It is later decided by
 * {@link ReferenceDeciderForType} implementations if the item becomes actually
 * part of the {@link de.qaware.openapigeneratorforspring.model.Components
 * Open Api model components}
 *
 * @param <T> type of the to be referenced item
 * @see ReferenceIdentifierBuilderForType
 */
public interface ReferencedItemConsumerForType<T> extends ReferencedItemConsumer {
    /**
     * Register given item as maybe to be referenced.
     *
     * @param item   item
     * @param setter setter when this item belongs to.
     * @implNote Implementations are required to call the setter at
     * least once. It is called again if the given item is referenced.
     */
    void maybeAsReference(T item, Consumer<T> setter);
}
