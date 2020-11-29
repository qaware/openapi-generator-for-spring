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

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Supplier for annotations present on the {@code @SpringBootApplication} class.
 *
 * <p>Does not extend AnnotationsSupplier to avoid accidental auto-wiring,
 * as AnnotationsSupplier are created from AnnotationsSupplierFactory.
 *
 * @see OpenApiSpringBootApplicationClassSupplier
 * @see OpenAPIDefinitionAnnotationSupplier
 */
@FunctionalInterface
public interface OpenApiSpringBootApplicationAnnotationsSupplier {
    <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType);

    default <A extends Annotation> Optional<A> findFirstAnnotation(Class<A> annotationType) {
        return findAnnotations(annotationType).findFirst();
    }
}
