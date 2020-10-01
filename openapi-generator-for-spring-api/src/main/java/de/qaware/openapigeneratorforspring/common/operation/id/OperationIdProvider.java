package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;

public interface OperationIdProvider {
    String getOperationId(OperationInfo operationInfo);
}
