package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.Ordered;

public interface OperationCustomizer extends Ordered {

    default void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        // do nothing by default
    }

    default void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                           io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        customize(operation, operationBuilderContext);
    }
}
