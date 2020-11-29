package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
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
    public Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        return handlerMethodParameter.getAnnotationsSupplier()
                .findAnnotations(annotationClass).findFirst()
                .map(this::buildParameter)
                .orElse(null);
    }

    @Nullable
    protected abstract Parameter buildParameter(A annotation);
}
