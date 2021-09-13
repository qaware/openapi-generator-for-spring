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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.INFER_FROM_OBJECT_MAPPER;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.ISO8601;
import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8.Java8TimeTypeResolverConfigurationProperties.Format.UNIX_EPOCH_SECONDS;

@RequiredArgsConstructor
public class DefaultJava8TimeInitialSchemaBuilder implements Java8TimeInitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    private final Java8TimeTypeResolverConfigurationProperties properties;
    @Nullable
    private final ObjectMapper objectMapper;

    @Nullable
    @Override
    public Schema buildFromType(SchemaResolver.Caller caller, InitialType initialType) {
        Class<?> rawClass = initialType.getType().getRawClass();
        if (rawClass.equals(Instant.class)) {
            Format format = getFormat(WRITE_DATES_AS_TIMESTAMPS);
            Schema.SchemaBuilder schemaBuilder = createSchemaBuilderWithType(format);
            if (format == ISO8601) {
                return schemaBuilder.format("date-time").build();
            }
            return schemaBuilder.build();
        } else if (rawClass.equals(Duration.class)) {
            Format format = getFormat(WRITE_DURATIONS_AS_TIMESTAMPS);
            return createSchemaBuilderWithType(format).build();
        }
        return null;
    }

    private Format getFormat(SerializationFeature serializationFeature) {
        if (properties.getFormat() == INFER_FROM_OBJECT_MAPPER) {
            if (objectMapper == null) {
                throw new IllegalStateException("Cannot infer format from object mapper without object mapper being present");
            }
            return objectMapper.isEnabled(serializationFeature) ? UNIX_EPOCH_SECONDS : ISO8601;
        }
        return properties.getFormat();
    }

    private static Schema.SchemaBuilder createSchemaBuilderWithType(Format format) {
        if (format == ISO8601) {
            return Schema.builder().type("string");
        } else if (format == UNIX_EPOCH_SECONDS) {
            return Schema.builder().type("number").format("int64");
        }
        throw new IllegalArgumentException("Unsupported Java8 Time Format " + format);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
