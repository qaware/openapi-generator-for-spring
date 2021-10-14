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
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilder;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class TypeResolverForMapLikeType implements InitialTypeBuilder, InitialSchemaBuilder, TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private final SchemaNameBuilder schemaNameBuilder;

    @Nullable
    @Override
    public InitialType build(SchemaResolver.Caller caller, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        if (javaType.isMapLikeType()) {
            return new MapLikeInitialType(javaType, annotationsSupplier);
        }
        return null;
    }

    @Nullable
    @Override
    public Schema buildFromType(SchemaResolver.Caller caller, InitialType initialType) {
        if (initialType instanceof MapLikeInitialType) {
            return new MapLikeSchema("MapOf" + schemaNameBuilder.buildFromType(caller, initialType.getType().getContentType()));
        }
        return null;
    }

    @Nullable
    @Override
    public RecursionKey resolve(SchemaResolver.Caller caller, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        if (initialType instanceof MapLikeInitialType) {
            JavaType contentType = initialType.getType().getContentType();
            schemaBuilderFromType.buildSchemaFromType(contentType, initialType.getAnnotationsSupplier(), schema::setAdditionalProperties);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @EqualsAndHashCode(callSuper = true)
    private static class MapLikeSchema extends Schema {
        public MapLikeSchema(String name) {
            setName(name);
            setType("object");
        }
    }

    private static class MapLikeInitialType extends InitialType {

        protected MapLikeInitialType(JavaType type, AnnotationsSupplier annotationsSupplier) {
            super(type, annotationsSupplier);
        }
    }
}
