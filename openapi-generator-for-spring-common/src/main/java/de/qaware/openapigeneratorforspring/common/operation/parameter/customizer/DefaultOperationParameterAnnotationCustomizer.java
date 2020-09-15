package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.parameter.ParameterAnnotationMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationParameterAnnotationCustomizer implements OperationParameterCustomizer {
    private final ParameterAnnotationMapper parameterAnnotationMapper;

    @Override
    public void customize(io.swagger.v3.oas.models.parameters.Parameter parameter, java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier, OperationBuilderContext operationBuilderContext) {
        Parameter parameterAnnotation = parameterAnnotationsSupplier.findFirstAnnotation(Parameter.class);
        if (parameterAnnotation != null) {
            parameterAnnotationMapper.applyFromAnnotation(parameter, parameterAnnotation, operationBuilderContext.getReferencedItemConsumerSupplier());
        }
    }
}
