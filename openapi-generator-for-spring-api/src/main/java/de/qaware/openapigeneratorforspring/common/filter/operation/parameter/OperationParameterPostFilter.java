package de.qaware.openapigeneratorforspring.common.filter.operation.parameter;

import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

/**
 * Filter for {@link Parameter Parameters}. Is evaluated AFTER the parameter is constructed.
 *
 * <p>Prefer using {@link OperationParameterPreFilter} whenever possible.
 *
 * @see OperationParameterPreFilter
 */
@FunctionalInterface
public interface OperationParameterPostFilter {
    /**
     * Accept the given parameter of the operation.
     *
     * @param parameter operation parameter
     * @return true if it shall be included, false otherwise
     */
    boolean postAccept(Parameter parameter);
}
