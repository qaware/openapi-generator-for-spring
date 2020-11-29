package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterAnnotationCustomizer implements OperationParameterCustomizer {
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter().ifPresent(
                handlerMethodParameter -> handlerMethodParameter.getAnnotationsSupplier()
                        .findAnnotations(io.swagger.v3.oas.annotations.Parameter.class).findFirst()
                        .ifPresent(parameterAnnotation ->
                                parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, context.getMapperContext(handlerMethodParameter.getContext()))
                        )
        );
    }
}
