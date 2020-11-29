package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

/**
 * Filter for {@link Operation Operations}. Is evaluated AFTER the operation is constructed.
 *
 * <p> This filter does not remove possibly referenced items due to operation
 * building. Prefer using {@link OperationPreFilter} whenever possible.
 *
 * @see OperationPreFilter
 */
@FunctionalInterface
public interface OperationPostFilter {
    /**
     * Accept the given operation.
     *
     * @param operation     built operation
     * @param handlerMethod handler method used to build this operation
     * @return true if it shall be included, false otherwise
     */
    boolean postAccept(Operation operation, HandlerMethod handlerMethod);
}
