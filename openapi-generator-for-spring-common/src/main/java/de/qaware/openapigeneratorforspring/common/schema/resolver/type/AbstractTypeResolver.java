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

import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTypeResolver implements TypeResolver {

    private final TypeResolverSupport typeResolverSupport;

    @Nullable
    @Override
    public final RecursionKey resolve(SchemaResolver.Mode mode, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        if (typeResolverSupport.supports(schema)) {
            return resolveIfSupported(mode, schema, initialType, schemaBuilderFromType);
        }
        return null;
    }

    @Nullable
    protected abstract RecursionKey resolveIfSupported(SchemaResolver.Mode mode, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType);

}
