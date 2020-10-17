package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.reference.component.securityscheme.ReferencedSecuritySchemesConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;

@RequiredArgsConstructor
public class DefaultOperationSecuritySchemesCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod());
        Map<String, SecurityScheme> securitySchemes = buildStringMapFromStream(
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityScheme.class),
                io.swagger.v3.oas.annotations.security.SecurityScheme::name,
                securitySchemeAnnotationMapper::map
        );
        operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSecuritySchemesConsumer.class)
                .accept(securitySchemes);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
