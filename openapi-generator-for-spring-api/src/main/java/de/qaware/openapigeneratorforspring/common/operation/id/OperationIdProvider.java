package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;

/**
 * Provide an ID for the {@link de.qaware.openapigeneratorforspring.model.operation.Operation Operation}.
 *
 * @see OperationIdConflictResolver for possible conflict resolution
 */
public interface OperationIdProvider {
    /**
     * Get non-unique ID for operation.
     *
     * @param operationInfo info to build the operation from
     * @return operation id, must not be unique but should be descriptive
     */
    String getOperationId(OperationInfo operationInfo);
}
