/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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

package de.qaware.openapigeneratorforspring.common.supplier;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Supplier for {@link OpenAPIDefinition}. Default implementation
 * looks at the class annotated with {@code @SpringBootApplication}.
 *
 * @see OpenApiSpringBootApplicationAnnotationsSupplier
 * @see OpenApiSpringBootApplicationClassSupplier
 */
@FunctionalInterface
public interface OpenAPIDefinitionAnnotationSupplier {
    /**
     * Get the annotation, if any.
     *
     * @return annotation, or empty optional if not found
     */
    Optional<OpenAPIDefinition> get();

    /**
     * Helper method to obtain values from the
     * {@link OpenAPIDefinition Open Api annotation}.
     *
     * @param mapper mapper to extract values
     * @param <A>    type of extracted annotations
     * @return stream of mapped annotations, can be empty
     */
    default <A extends Annotation> Stream<A> getAnnotations(Function<OpenAPIDefinition, A[]> mapper) {
        return get().map(mapper).map(Arrays::stream).orElseGet(Stream::empty);
    }
}
