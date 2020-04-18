package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import io.swagger.v3.oas.models.parameters.Parameter;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public abstract class ParameterMethodConverterFromAnnotation<A extends Annotation> implements ParameterMethodConverter {

    private final Class<A> annotationClass;

    protected ParameterMethodConverterFromAnnotation(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Nullable
    @Override
    public Parameter convert(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier) {
        A annotation = parameterAnnotationsSupplier.findFirstAnnotation(annotationClass);
        if (annotation != null) {
            return buildParameter(annotation);
        }
        return null;
    }

    protected abstract Parameter buildParameter(A annotation);
}
