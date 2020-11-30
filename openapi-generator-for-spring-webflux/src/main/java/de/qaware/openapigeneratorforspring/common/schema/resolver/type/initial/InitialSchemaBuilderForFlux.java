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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForFlux implements InitialSchemaBuilder, TypeResolverSupport {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public Schema buildFromType(JavaType javaType) {
        if (javaType.getRawClass().equals(Flux.class)) {
            return new FluxSchema();
        }
        return null;
    }

    @EqualsAndHashCode(callSuper = true)
    private static class FluxSchema extends Schema {
        public FluxSchema() {
            setType("array");
        }
    }

    @Override
    public boolean supports(Schema schema) {
        return schema instanceof FluxSchema;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
