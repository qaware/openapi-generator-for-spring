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

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.HasReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import org.springframework.util.MimeType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

/**
 * Context swagger annotations mapping.
 *
 * <p>Enables the following:
 * <ul>
 *
 *  <li>Referencing items such as Schemas and others
 *  including ownership (see implementations of {@link
 *  de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer
 *  ReferencedItemConsumer})</li>
 *
 *  <li>{@link MediaTypesProvider Provides Media types} when mapping {@link
 *  io.swagger.v3.oas.annotations.media.Content content annotation}</li>
 *
 * </ul>
 */
public interface MapperContext extends HasReferencedItemConsumer {

    /**
     * Find the media types for the given owning type having {@link
     * de.qaware.openapigeneratorforspring.model.media.Content content}.
     *
     * @param owningType owning type, must extend {@link HasContent HasContent}
     * @return media types, or empty optional if nothing can be provided for this owning type
     */
    Optional<Set<MimeType>> findMimeTypes(Class<? extends HasContent> owningType);

    /**
     * Set the owner for any following referenced item.
     *
     * @param owner owner, or null if it shall be reset
     * @return mapper context with modified owner
     */
    MapperContext withReferencedItemOwner(@Nullable Object owner);

    /**
     * Sets the {@link MediaTypesProvider media types provider} for any
     * following calls of {@link #findMimeTypes}.
     *
     * @param mediaTypesProvider media types provider
     * @return mapper context with modified media types provider
     */
    MapperContext withMediaTypesProvider(MediaTypesProvider mediaTypesProvider);
}
