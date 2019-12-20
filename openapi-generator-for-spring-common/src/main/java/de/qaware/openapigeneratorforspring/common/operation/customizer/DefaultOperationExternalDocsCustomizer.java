package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.Ordered;

public class DefaultOperationExternalDocsCustomizer implements OperationCustomizer, Ordered {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        // fallback to the Swagger Core implementation (not nice but should be working)
        AnnotationsUtils.getExternalDocumentation(operationAnnotation.externalDocs())
                .ifPresent(operation::setExternalDocs);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
