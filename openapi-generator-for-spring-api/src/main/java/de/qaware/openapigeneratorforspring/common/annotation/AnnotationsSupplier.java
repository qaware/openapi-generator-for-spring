package de.qaware.openapigeneratorforspring.common.annotation;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;

@FunctionalInterface
public interface AnnotationsSupplier {

    AnnotationsSupplier EMPTY = new AnnotationsSupplier() {
        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return Stream.empty();
        }
    };

    static <T extends HasAnnotationsSupplier> AnnotationsSupplier merge(Stream<T> stream) {
        return stream.map(HasAnnotationsSupplier::getAnnotationsSupplier)
                .reduce(AnnotationsSupplier::andThen)
                .orElse(AnnotationsSupplier.EMPTY);
    }

    /**
     * Supply annotations for entity under investigation. Annotations
     * with highest precedence should appear first in returned stream.
     *
     * @param annotationType type of annotations to find
     * @param <A>            annotation class type
     * @return stream of found annotation of given type
     */
    <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType);

    @Nullable
    default <A extends Annotation> A findFirstAnnotation(Class<A> annotationType) {
        return findAnnotations(annotationType)
                .findFirst()
                .orElse(null);
    }

    default AnnotationsSupplier andThen(AnnotationsSupplier anotherAnnotationsSupplier) {
        AnnotationsSupplier annotationsSupplier = this;
        return new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                return Stream.concat(annotationsSupplier.findAnnotations(annotationType), anotherAnnotationsSupplier.findAnnotations(annotationType));
            }
        };
    }


}
