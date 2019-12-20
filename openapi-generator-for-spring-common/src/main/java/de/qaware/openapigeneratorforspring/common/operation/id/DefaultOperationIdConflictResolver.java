package de.qaware.openapigeneratorforspring.common.operation.id;

import io.swagger.v3.oas.models.Operation;

import java.util.List;

public class DefaultOperationIdConflictResolver implements OperationIdConflictResolver {
    @Override
    public void resolveConflict(String operationId, List<OperationIdConflict> operationIdConflicts) {
        for (int i = 0; i < operationIdConflicts.size(); i++) {
            Operation operation = operationIdConflicts.get(i).getOperation();
            operation.operationId(operationId + i);
        }
    }
}
