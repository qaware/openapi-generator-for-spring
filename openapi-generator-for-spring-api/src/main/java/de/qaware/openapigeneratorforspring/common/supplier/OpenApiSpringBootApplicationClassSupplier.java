package de.qaware.openapigeneratorforspring.common.supplier;

import java.util.Optional;

/**
 * Supplier for the class type carrying the
 * {@code @SpringBootApplication} annotation.
 *
 * @see OpenApiSpringBootApplicationAnnotationsSupplier
 * @see OpenAPIDefinitionAnnotationSupplier
 */
@FunctionalInterface
public interface OpenApiSpringBootApplicationClassSupplier {
    Optional<Class<?>> findSpringBootApplicationClass();
}
