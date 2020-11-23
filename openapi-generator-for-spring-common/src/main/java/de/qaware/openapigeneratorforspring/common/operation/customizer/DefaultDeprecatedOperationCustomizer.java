package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDeprecatedOperationCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        if (operation.getDeprecated() != null) {
            return;
        }
        operationBuilderContext.getOperationInfo().getHandlerMethod()
                .findAnnotations(Deprecated.class)
                .findAny()
                .ifPresent(ignored -> operation.setDeprecated(true));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
