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
