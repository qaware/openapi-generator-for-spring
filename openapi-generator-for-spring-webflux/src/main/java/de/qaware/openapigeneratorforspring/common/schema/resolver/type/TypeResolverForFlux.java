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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForFlux;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

public class TypeResolverForFlux extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    public TypeResolverForFlux(InitialSchemaBuilderForFlux typeResolverSupport) {
        super(typeResolverSupport);
    }

    @Override
    @Nullable
    public RecursionKey resolveIfSupported(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
        schemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, schema::setItems);
        return null; // Flux never creates cyclic schema dependencies
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
