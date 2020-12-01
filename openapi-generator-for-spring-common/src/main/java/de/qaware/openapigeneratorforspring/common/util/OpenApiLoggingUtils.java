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

package de.qaware.openapigeneratorforspring.common.util;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class OpenApiLoggingUtils {

    private static final Map<Class<?>, Function<Object, String>> PRETTY_PRINTERS = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void registerPrettyPrinter(Class<T> clazz, Function<T, String> prettyPrinter) {
        LOGGER.debug("Registering pretty printer for {}", clazz.getSimpleName());
        PRETTY_PRINTERS.put(clazz, object -> prettyPrinter.apply((T) object));
    }

    public static Object logLazily(Supplier<String> stringSupplier) {
        return new Object() {
            @Override
            public String toString() {
                return stringSupplier.get();
            }
        };
    }

    public interface HasToPrettyString {
        String toPrettyString();
    }

    public static Object logPretty(Object object) {
        return logLazily(() -> {
            if (object instanceof HasToPrettyString) {
                return ((HasToPrettyString) object).toPrettyString();
            }
            return PRETTY_PRINTERS.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(object.getClass()))
                    .findAny()
                    .map(entry -> entry.getValue().apply(object))
                    .orElseGet(object::toString);
        });
    }

    public static String prettyPrintSchema(Schema schema) {
        StringBuilder sb = new StringBuilder();
        if (schema.getNullable() != null && schema.getNullable()) {
            sb.append('?');
        }
        if (schema.getName() != null) {
            sb.append(schema.getName());
        } else if (schema.getItems() != null) {
            sb.append('[').append(prettyPrintSchema(schema.getItems())).append(']');
        } else if (schema.getType() != null) {
            sb.append("type:").append(schema.getType());
        } else if (schema.getRef() != null) {
            sb.append("-->").append(schema.getRef());
        }
        if (schema.getProperties() != null) {
            sb.append('{');
            schema.getProperties().forEach((propertyName, propertySchema) -> {
                sb.append(propertyName);
                sb.append('=');
                sb.append(prettyPrintSchema(propertySchema));
                sb.append(',');
            });
            sb.append("}");
        }
        return sb.toString();
    }

}
