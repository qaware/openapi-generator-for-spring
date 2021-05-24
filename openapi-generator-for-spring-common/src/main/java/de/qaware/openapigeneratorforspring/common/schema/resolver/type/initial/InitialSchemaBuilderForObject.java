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

import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@RequiredArgsConstructor
public class InitialSchemaBuilderForObject implements InitialSchemaBuilder, TypeResolverSupport {

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = laterThan(DEFAULT_ORDER);

    private final SchemaNameBuilder schemaNameBuilder;

    @Nullable
    @Override
    public Schema buildFromType(InitialType initialType) {
        return new ObjectSchema(schemaNameBuilder.buildFromType(initialType.getType()));
    }

    @EqualsAndHashCode(callSuper = true)
    private static class ObjectSchema extends Schema {
        public ObjectSchema(String name) {
            setType("object");
            setName(name);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    public boolean supports(Schema schema) {
        return schema instanceof ObjectSchema;
    }
}
