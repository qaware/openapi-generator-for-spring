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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilderForCollectionLikeType.ArraySchemaInitialType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;

@RequiredArgsConstructor
public class InitialSchemaBuilderForCollectionLikeType implements InitialSchemaBuilder, TypeResolverSupport {

    public static final int ORDER = DEFAULT_ORDER;

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public Schema buildFromType(InitialType initialType) {
        if (initialType instanceof ArraySchemaInitialType) {
            val arraySchemaInitialType = (ArraySchemaInitialType) initialType;
            return buildArraySchema(arraySchemaInitialType.getArraySchemaAnnotations(), Set.class.isAssignableFrom(initialType.getType().getRawClass()));
        }
        return null;
    }

    private ArraySchema buildArraySchema(LinkedList<io.swagger.v3.oas.annotations.media.ArraySchema> arraySchemaAnnotations, boolean isSetType) {
        ArraySchema arraySchema = new ArraySchema(isSetType ? true : null);
        arraySchemaAnnotations.descendingIterator()
                .forEachRemaining(arraySchemaAnnotation -> applyFromAnnotation(arraySchema, arraySchemaAnnotation));
        return arraySchema;
    }

    private void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.ArraySchema annotation) {
        if (annotation.uniqueItems()) {
            schema.setUniqueItems(true);
        }
        if (annotation.minItems() != Integer.MAX_VALUE) {
            schema.setMinItems(annotation.minItems());
        }
        if (annotation.maxItems() != Integer.MIN_VALUE) {
            schema.setMaxItems(annotation.maxItems());
        }
        mergeWithExistingMap(schema::getExtensions, schema::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
    }

    @Override
    public boolean supports(Schema schema) {
        return schema instanceof ArraySchema;
    }

    @EqualsAndHashCode(callSuper = true)
    private static class ArraySchema extends Schema {
        public ArraySchema(@Nullable Boolean uniqueItems) {
            setType("array");
            setUniqueItems(uniqueItems);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
