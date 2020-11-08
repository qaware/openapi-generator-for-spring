package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

public abstract class AbstractHandlerMethodParameterCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, @Nullable HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext) {
        if (handlerMethodParameter != null) {
            customizeWithHandlerMethod(parameter, handlerMethodParameter, operationBuilderContext);
        }
    }

    protected abstract void customizeWithHandlerMethod(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext);
}
