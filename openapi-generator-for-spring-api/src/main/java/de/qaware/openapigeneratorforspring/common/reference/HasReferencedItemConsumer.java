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

/**
 * Trait for providing a {@link ReferencedItemConsumer}
 * of specific sub-type. Typically implemented by using
 * a {@link ReferencedItemConsumerSupplier} instance.
 */
public interface HasReferencedItemConsumer {
    /**
     * Get the specific referenced item consumer.
     *
     * @param referencedItemConsumerClazz type of the consumer
     * @param <C>                         consumer type, extends {@link ReferencedItemConsumer}.
     * @return referenced item consumer
     */
    <C extends ReferencedItemConsumer> C getReferencedItemConsumer(Class<C> referencedItemConsumerClazz);
}
