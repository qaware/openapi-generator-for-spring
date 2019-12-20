package de.qaware.openapigeneratorforspring.common.operation.id;

import java.util.List;

public interface OperationIdConflictResolver {
    void resolveConflict(String operationId, List<OperationIdConflict> operationIdConflicts);
}
