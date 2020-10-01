package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationWithInfo;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

import java.util.List;

public class DefaultOperationIdConflictResolver implements OperationIdConflictResolver {
    @Override
    public void resolveConflict(String operationId, List<OperationWithInfo> operationsWithConflicts) {
        for (int i = 0; i < operationsWithConflicts.size(); i++) {
            Operation operation = operationsWithConflicts.get(i).getOperation();
            operation.setOperationId(operationId + i);
        }
    }
}
