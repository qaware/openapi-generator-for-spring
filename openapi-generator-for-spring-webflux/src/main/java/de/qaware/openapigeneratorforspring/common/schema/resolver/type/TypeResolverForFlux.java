/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebFlux
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
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class TypeResolverForFlux implements InitialTypeBuilder, InitialSchemaBuilder, TypeResolver {
    public static final int ORDER = DEFAULT_ORDER;

    private final TypeResolverForCollectionLikeTypeSupport support;

    @Nullable
    @Override
    public InitialType build(SchemaResolver.Caller caller, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        return support.build(this, type -> type.getRawClass().equals(Flux.class), javaType, annotationsSupplier);
    }

    @Nullable
    @Override
    public Schema buildFromType(SchemaResolver.Caller caller, InitialType initialType) {
        return support.buildFromType(this, initialType);
    }

    @Nullable
    @Override
    public RecursionKey resolve(SchemaResolver.Caller caller, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        return support.resolve(this, schema, initialType, () -> initialType.getType().getBindings().getTypeParameters().iterator().next(), schemaBuilderFromType);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
