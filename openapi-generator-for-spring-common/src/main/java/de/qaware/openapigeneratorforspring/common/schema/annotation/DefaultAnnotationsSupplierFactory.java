package de.qaware.openapigeneratorforspring.common.schema.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultAnnotationsSupplierFactory implements AnnotationsSupplierFactory {

    @Override
    public AnnotationsSupplier createForMember(AnnotatedMember annotatedMember) {
        return new AnnotationSupplierForMember(annotatedMember);
    }

    @Override
    public AnnotationsSupplier createForClass(Class<?> clazz) {
        return new AnnotationsSupplierForClass(clazz);
    }

    private static class AnnotationSupplierForMember implements AnnotationsSupplier {
        private final AnnotatedMember annotatedMember;
        private final AnnotationsSupplierForClass supplierForType;

        public AnnotationSupplierForMember(AnnotatedMember annotatedMember) {
            this.annotatedMember = annotatedMember;
            this.supplierForType = new AnnotationsSupplierForClass(annotatedMember.getType().getRawClass());
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            Stream<A> annotationFromMember = Optional.ofNullable(annotatedMember.getAnnotation(annotationType))
                    .map(Stream::of)
                    .orElse(Stream.empty());
            return Stream.concat(annotationFromMember, supplierForType.findAnnotations(annotationType));
        }
    }

    private static class AnnotationsSupplierForClass implements AnnotationsSupplier {
        private final MergedAnnotations mergedAnnotations;

        public AnnotationsSupplierForClass(Class<?> clazz) {
            this.mergedAnnotations = MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY, RepeatableContainers.none());
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            return mergedAnnotations.stream(annotationType)
                    .map(MergedAnnotation::synthesize);
        }
    }
}
