package de.qaware.openapigeneratorforspring.common.filter.operation;

import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

/**
 * Filter for {@link Operation Operations}. Is evaluated BEFORE the operation is constructed.
 *
 * @see OperationPostFilter
 */
@FunctionalInterface
public interface OperationPreFilter {
    /**
     * Accept the given operation info to build an operation from.
     *
     * @param operationInfo operation info
     * @return true if operation shall be built, false otherwise
     */
    boolean preAccept(OperationInfo operationInfo);
}
