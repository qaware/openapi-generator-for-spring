package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;

public class DefaultOperationIdProvider implements OperationIdProvider {
    @Override
    public String getOperationId(OperationBuilderContext operationBuilderContext) {
        return operationBuilderContext.getHandlerMethod().getMethod().getName();
    }
}
