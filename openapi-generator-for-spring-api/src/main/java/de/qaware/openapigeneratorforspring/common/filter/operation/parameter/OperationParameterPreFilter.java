package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

@FunctionalInterface
public interface OperationParameterPreFilter {
    boolean preAccept(HandlerMethod.Parameter handlerMethodParameter);
}
