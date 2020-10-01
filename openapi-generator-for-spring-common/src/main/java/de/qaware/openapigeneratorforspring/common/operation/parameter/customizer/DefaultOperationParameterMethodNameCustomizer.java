package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultOperationParameterMethodNameCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier, OperationBuilderContext operationBuilderContext) {
        if (!methodParameter.isNamePresent()) {
            return;
        }
        String parameterName = parameter.getName();
        String methodParameterName = methodParameter.getName();
        if (parameterName == null) {
            parameter.setName(methodParameterName);
        } else if (!parameterName.equals(methodParameterName)) {
            LOGGER.warn("Parameter name {} different from parameter variable name {}", parameterName, methodParameterName);
        }
    }
}
