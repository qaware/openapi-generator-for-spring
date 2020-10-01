package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface OperationCustomizer extends Ordered {

    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    default void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        // do nothing by default
    }

    default void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                                io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        customize(operation, operationBuilderContext);
    }
}
