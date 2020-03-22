package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
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
            this.mergedAnnotations = MergedAnnotations.from(annotatedElement, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            // TODO investigate why explicitly finding this annotation might return something while
            // the approach via mergedAnnotations doesn't
            Stream<A> annotationOnMethod = Optional.ofNullable(AnnotationUtils.findAnnotation(annotatedElement, annotationType))
                    .map(Stream::of)
                    .orElse(Stream.empty());
            return Stream.concat(
                    annotationOnMethod,
                    mergedAnnotations.stream(annotationType).map(MergedAnnotation::synthesize)
            );
        }
    }
}
