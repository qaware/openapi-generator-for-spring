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

package de.qaware.openapigeneratorforspring.common.annotation;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * Abstraction for obtaining annotations. Is created with {@link AnnotationsSupplierFactory}.
 */
@FunctionalInterface
public interface AnnotationsSupplier {

    /**
     * Empty supplier, never finding any annotations.
     */
    AnnotationsSupplier EMPTY = new AnnotationsSupplier() {
        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return Stream.empty();
        }
    };

    /**
     * Supply annotations for entity under investigation. Annotations
     * with highest precedence should appear first in returned stream.
     *
     * @param annotationType type of annotations to find
     * @param <A>            annotation class type
     * @return stream of found annotation of given type
     */
    <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType);

    /**
     * Get new annotation supplier applying another
     * annotations supplier after this one.
     *
     * @param anotherAnnotationsSupplier another annotations supplier
     * @return combined annotations supplier searching this one first, then given another supplier
     * @see #merge
     */
    default AnnotationsSupplier andThen(AnnotationsSupplier anotherAnnotationsSupplier) {
        AnnotationsSupplier annotationsSupplier = this;
        return new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                return Stream.concat(annotationsSupplier.findAnnotations(annotationType), anotherAnnotationsSupplier.findAnnotations(annotationType));
            }
        };
    }

    /**
     * Merge the {@link HasAnnotationsSupplier items having
     * annotations suppliers} into one merged annotations supplier.
     *
     * @param stream stream of items with annotations suppliers.
     * @param <T>    type having annotation suppliers.
     * @return annotation supplier querying the annotation suppliers of the given items
     */
    static <T extends HasAnnotationsSupplier> AnnotationsSupplier merge(Stream<T> stream) {
        return stream.map(HasAnnotationsSupplier::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
    }
}
