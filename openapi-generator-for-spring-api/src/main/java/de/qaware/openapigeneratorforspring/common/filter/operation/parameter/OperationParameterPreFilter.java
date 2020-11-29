package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

/**
 * Filter for {@link Parameter Parameters}. Is evaluated BEFORE the parameter is constructed.
 *
 * @see OperationParameterPostFilter
 */
@FunctionalInterface
public interface OperationParameterPreFilter {
    /**
     * Accept the given parameter of the handler method to build an operation parameter from.
     *
     * @param handlerMethodParameter handler method parameter
     * @return true if parameter shall be built, false otherwise
     */
    boolean preAccept(HandlerMethod.Parameter handlerMethodParameter);
}
