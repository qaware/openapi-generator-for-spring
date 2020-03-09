package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationExtensionsCustomizer implements OperationCustomizer {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext, io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        setMapIfNotEmpty(
                extensionAnnotationMapper.mapArray(operationAnnotation.extensions()),
                operation::setExtensions
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
