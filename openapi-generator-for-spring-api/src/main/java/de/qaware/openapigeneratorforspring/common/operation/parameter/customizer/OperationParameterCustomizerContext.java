package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import java.util.Optional;

public interface OperationParameterCustomizerContext extends OperationBuilderContext {
    Optional<HandlerMethod.Parameter> getHandlerMethodParameter();
}
