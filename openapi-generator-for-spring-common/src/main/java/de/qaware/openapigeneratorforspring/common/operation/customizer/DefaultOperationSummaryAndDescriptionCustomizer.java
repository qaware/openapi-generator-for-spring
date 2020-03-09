package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import org.springframework.core.Ordered;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class DefaultOperationSummaryAndDescriptionCustomizer implements OperationCustomizer {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setStringIfNotBlank(operationAnnotation.summary(), operation::setSummary);
        setStringIfNotBlank(operationAnnotation.description(), operation::setDescription);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
