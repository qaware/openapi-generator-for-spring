package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.SecurityRequirementAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.ensureKeyIsNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationSecurityRequirementCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final SecurityRequirementAnnotationMapper securityRequirementAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod());

        // the following code assumes that all security requirements we find must be fulfilled at the same time,
        // it's more safe to assume that multiple requirements "add up" instead of requiring them as OR
        // the spec allows also an OR case, for which swagger annotation support seems to be missing though
        SecurityRequirement securityRequirement = annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityRequirement.class)
                .map(securityRequirementAnnotationMapper::map)
                .flatMap(x -> x.entrySet().stream())
                .collect(Collectors.toMap(ensureKeyIsNotBlank(Map.Entry::getKey), Map.Entry::getValue, (a, b) -> {
                    throw new IllegalStateException("Conflicting security requirement annotation with same name found: " + a + " vs. " + b);
                }, SecurityRequirement::new));
        // apply later from operation to overwrite them!
        securityRequirement.putAll(operation.getFirstSecurity().orElseGet(SecurityRequirement::new));
        setMapIfNotEmpty(securityRequirement, operation::setFirstSecurity);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
