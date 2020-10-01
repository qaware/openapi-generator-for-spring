package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;

import java.util.List;

public interface OperationIdConflictResolver {
    void resolveConflict(String operationId, List<OperationWithInfo> operationsWithConflicts);
}
