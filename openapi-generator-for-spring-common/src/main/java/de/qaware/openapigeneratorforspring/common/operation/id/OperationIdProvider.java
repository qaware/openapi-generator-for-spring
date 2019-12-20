package de.qaware.openapigeneratorforspring.common.operation.id;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;

public interface OperationIdProvider {
    String getOperationId(OperationBuilderContext operationBuilderContext);
}
