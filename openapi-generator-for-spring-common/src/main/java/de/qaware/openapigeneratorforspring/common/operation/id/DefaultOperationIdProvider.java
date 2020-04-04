package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;

public class DefaultOperationIdProvider implements OperationIdProvider {
    @Override
    public String getOperationId(OperationInfo operationInfo) {
        return operationInfo.getHandlerMethod().getMethod().getName();
    }
}
