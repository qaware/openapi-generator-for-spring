package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

@RequiredArgsConstructor
public class DefaultOperationExternalDocsCustomizer implements OperationCustomizer {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        externalDocumentationAnnotationMapper.map(operationAnnotation.externalDocs())
                .ifPresent(operation::setExternalDocs);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
