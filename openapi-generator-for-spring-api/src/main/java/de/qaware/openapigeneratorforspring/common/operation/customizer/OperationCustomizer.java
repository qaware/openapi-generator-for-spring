package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface OperationCustomizer extends Ordered {

    int DEFAULT_ORDER = 0;

    void customize(Operation operation, OperationBuilderContext operationBuilderContext);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
