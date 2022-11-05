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

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.trait.HasContent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.util.MimeType;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperContextImpl implements MapperContext {
    private final ReferencedItemConsumerSupplier referencedItemConsumerSupplier;
    @Nullable
    @With
    private final MediaTypesProvider mediaTypesProvider;

    public static MapperContextImpl of(ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return new MapperContextImpl(referencedItemConsumerSupplier, null);
    }

    @Override
    public <T extends ReferencedItemConsumer> T getReferencedItemConsumer(Class<T> referencedItemConsumerClazz) {
        return referencedItemConsumerSupplier.get(referencedItemConsumerClazz);
    }

    @Override
    public Optional<Set<MimeType>> findMimeTypes(Class<? extends HasContent> owningType) {
        return Optional.ofNullable(mediaTypesProvider).map(provider -> provider.getMimeTypes(owningType));
    }

    @Override
    public MapperContext withReferencedItemOwner(@Nullable Object owner) {
        return new MapperContextImpl(referencedItemConsumerSupplier.withOwner(owner), mediaTypesProvider);
    }
}
