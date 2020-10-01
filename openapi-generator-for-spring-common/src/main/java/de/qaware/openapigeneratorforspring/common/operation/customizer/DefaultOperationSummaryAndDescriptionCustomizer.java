package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class DefaultOperationSummaryAndDescriptionCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setStringIfNotBlank(operationAnnotation.summary(), operation::setSummary);
        setStringIfNotBlank(operationAnnotation.description(), operation::setDescription);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
