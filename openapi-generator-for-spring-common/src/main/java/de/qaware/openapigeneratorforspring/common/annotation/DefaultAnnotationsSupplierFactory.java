package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationFilter;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultAnnotationsSupplierFactory implements AnnotationsSupplierFactory {

    @Override
    public AnnotationsSupplier createFromMember(AnnotatedMember annotatedMember) {
        // using annotatedMember.getAnnotatedElement() and falling back
        // to the other method is not correct, as it doesn't find annotations specified on fields
        return new AnnotationSupplierForMember(annotatedMember);
    }

    @Override
    public AnnotationsSupplier createFromAnnotatedElement(AnnotatedElement annotatedElement) {
        return new AnnotationsSupplierFromAnnotatedElement(annotatedElement);
    }

    @RequiredArgsConstructor
    private static class AnnotationSupplierForMember implements AnnotationsSupplier {
        private final AnnotatedMember annotatedMember;

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return Optional.ofNullable(annotatedMember.getAnnotation(annotationType))
                    .map(Stream::of)
                    .orElse(Stream.empty());
        }
    }

    private static class AnnotationsSupplierFromAnnotatedElement implements AnnotationsSupplier {
        private final AnnotatedElement annotatedElement;
        private final MergedAnnotations mergedAnnotations;

        public AnnotationsSupplierFromAnnotatedElement(AnnotatedElement annotatedElement) {
            this.annotatedElement = annotatedElement;
            this.mergedAnnotations = MergedAnnotations.from(
                    annotatedElement,
                    MergedAnnotations.SearchStrategy.TYPE_HIERARCHY,
                    RepeatableContainers.standardRepeatables(),
                    AnnotationFilter.NONE // broken, see https://github.com/spring-projects/spring-framework/issues/24932
            );
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            if (AnnotationFilter.PLAIN.matches(annotationType)) {
                // This specialized treatment can be
                // removed once https://github.com/spring-projects/spring-framework/issues/24932 is fixed
                return Arrays.stream(annotatedElement.getAnnotationsByType(annotationType));
            } else {
                return mergedAnnotations.stream(annotationType).map(MergedAnnotation::synthesize);
            }
        }
    }
}
