package de.qaware.openapigeneratorforspring.common.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface OpenAPIDefinitionAnnotationSupplier {
    Optional<OpenAPIDefinition> get();

    default <A extends Annotation> Stream<A> getValues(Function<OpenAPIDefinition, A[]> mapper) {
        return get().map(mapper).map(Arrays::stream).orElseGet(Stream::empty);
    }
}
