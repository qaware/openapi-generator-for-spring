package de.qaware.openapigeneratorforspring.common.schema.annotation;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@FunctionalInterface
public interface AnnotationsSupplier {
    /**
     * Supply annotations for entity under investigation. Annotations
     * with highest precedence should appear first in returned stream.
     *
     * @param annotationType type of annotations to find
     * @param <A>            annotation class type
     * @return stream of found annotation of given type
     */
    <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType);
}
