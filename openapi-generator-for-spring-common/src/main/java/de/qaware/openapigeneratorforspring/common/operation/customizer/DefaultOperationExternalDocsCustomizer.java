package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOperationExternalDocsCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        externalDocumentationAnnotationMapper.map(operationAnnotation.externalDocs())
                .ifPresent(operation::setExternalDocs);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
