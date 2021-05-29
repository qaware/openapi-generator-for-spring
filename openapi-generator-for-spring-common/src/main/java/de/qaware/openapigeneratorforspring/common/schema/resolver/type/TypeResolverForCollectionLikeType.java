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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilder;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Set;

@RequiredArgsConstructor
public class TypeResolverForCollectionLikeType implements InitialTypeBuilder, InitialSchemaBuilder, TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private final TypeResolverForCollectionLikeTypeSupport support;

    @Nullable
    @Override
    public InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        return support.build(this, JavaType::isCollectionLikeType, javaType, annotationsSupplier);
    }

    @Nullable
    @Override
    public Schema buildFromType(InitialType initialType) {
        Schema schema = support.buildFromType(this, initialType);
        setUniqueItemsIfAssignableToSet(schema, initialType.getType());
        return schema;
    }

    @Nullable
    @Override
    public RecursionKey resolve(SchemaResolver.Mode mode, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        return support.resolve(this, schema, initialType, () -> initialType.getType().getContentType(), schemaBuilderFromType);
    }

    private void setUniqueItemsIfAssignableToSet(@Nullable Schema schema, JavaType type) {
        if (schema != null && Set.class.isAssignableFrom(type.getRawClass())) {
            schema.setUniqueItems(true);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
