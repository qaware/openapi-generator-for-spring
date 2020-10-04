package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ParameterMethodConverterFromAnnotation<A extends Annotation> implements ParameterMethodConverter {

    private final Class<A> annotationClass;

    @Nullable
    @Override
    public Parameter convert(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        A annotation = parameterAnnotationsSupplier.findFirstAnnotation(annotationClass);
        if (annotation != null) {
            return buildParameter(annotation);
        }
        return null;
    }

    @Nullable
    protected abstract Parameter buildParameter(A annotation);
}
