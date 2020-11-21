package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultOperationParameterMethodNameCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter()
                .flatMap(HandlerMethod.Parameter::getName)
                .ifPresent(methodParameterName -> {
                    String parameterName = parameter.getName();
                    if (parameterName == null) {
                        parameter.setName(methodParameterName);
                    } else if (!parameterName.equals(methodParameterName)) {
                        LOGGER.warn("Parameter name {} different from parameter variable name {}", parameterName, methodParameterName);
                    }
                });
    }
}
