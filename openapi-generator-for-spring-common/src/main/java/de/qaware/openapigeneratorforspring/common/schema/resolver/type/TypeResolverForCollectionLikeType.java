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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForCollectionLikeType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class TypeResolverForCollectionLikeType extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    public TypeResolverForCollectionLikeType(InitialSchemaBuilderForCollectionLikeType typeResolverSupport) {
        super(typeResolverSupport);
    }

    @Nullable
    @Override
    protected RecursionKey resolveInternal(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema?
        // TODO append annotationSupplier with contained generic type!
        Schema schema = initialSchema.getSchema();
        schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema::setItems);
        return null; // collections never create cyclic dependencies
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
