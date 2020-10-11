package de.qaware.openapigeneratorforspring.common.util;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultOpenApiSpringBootApplicationAnnotationsSupplier implements OpenApiSpringBootApplicationAnnotationsSupplier {
    private final OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
        return springBootApplicationClassSupplier.findSpringBootApplicationClass()
                .map(annotationsSupplierFactory::createFromAnnotatedElement)
                .map(annotationsSupplier -> annotationsSupplier.findAnnotations(annotationType))
                .orElseGet(Stream::empty);
    }
}
