package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;

import java.util.List;

/**
 * Resolver for non-unique operation IDs. See also {@link OperationIdProvider}.
 */
public interface OperationIdConflictResolver {
    /**
     * Resolve a operation id conflict by modifying the given operations via reference.
     * <p>
     * OpenApi model building will fail if conflict hasn't been resolved.
     *
     * @param operationId             conflicting operation id
     * @param operationsWithConflicts operations with conflicts and infos.
     */
    void resolveConflict(String operationId, List<OperationWithInfo> operationsWithConflicts);
}
