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
