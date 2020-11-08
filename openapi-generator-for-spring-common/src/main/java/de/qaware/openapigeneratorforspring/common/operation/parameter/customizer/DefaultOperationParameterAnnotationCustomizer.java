package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterAnnotationCustomizer implements OperationParameterCustomizer {
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        Parameter parameterAnnotation = handlerMethodParameter.getAnnotationsSupplier().findFirstAnnotation(Parameter.class);
        if (parameterAnnotation != null) {
            parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, operationBuilderContext.getMapperContext());
        }
    }
}
