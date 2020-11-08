package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterAnnotationCustomizer implements OperationParameterCustomizer {
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(handlerMethodParameter -> {
            Parameter parameterAnnotation = handlerMethodParameter.getAnnotationsSupplier().findFirstAnnotation(Parameter.class);
            if (parameterAnnotation != null) {
                parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, context.getMapperContext());
            }
        });
    }
}
