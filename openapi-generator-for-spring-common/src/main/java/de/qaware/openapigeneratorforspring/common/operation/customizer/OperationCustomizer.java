package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;

public interface OperationCustomizer {
    void customize(Operation operation, OperationBuilderContext operationBuilderContext);
}
