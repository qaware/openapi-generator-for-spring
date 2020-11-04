package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;


@FunctionalInterface
public interface OperationParameterCustomizer {
    void customize(Parameter parameter, HandlerMethod.Parameter handlerMethodParameter, OperationBuilderContext operationBuilderContext);
}
