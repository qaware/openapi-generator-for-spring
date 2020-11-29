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

package de.qaware.openapigeneratorforspring.common.reference;


import javax.annotation.Nullable;

/**
 * Supplier for referenced item consumers. Provided during open api
 * model building to collect to-be-referenced items of different types.
 */
public interface ReferencedItemConsumerSupplier {
    /**
     * Get a referenced item consumer of specific type.
     *
     * @param referencedItemConsumerClazz type of consumer
     * @param <C>                         consumer type, extends {@link ReferencedItemConsumer}.
     * @return reference item consumer, will fail if nothing is found
     */
    <C extends ReferencedItemConsumer> C get(Class<C> referencedItemConsumerClazz);

    /**
     * Set the owner for all following consumed items via {@link #get}.
     *
     * @param owner owner, or null if no owner
     * @return modified referenced item consumer supplier
     */
    ReferencedItemConsumerSupplier withOwner(@Nullable Object owner);
}
