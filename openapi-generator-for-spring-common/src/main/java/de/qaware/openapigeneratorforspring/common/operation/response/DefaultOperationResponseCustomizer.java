package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.util.OpenApiProxyUtils;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ApiResponseAnnotationMapper apiResponseAnnotationMapper;
    private final List<OperationApiResponsesCustomizer> operationApiResponsesCustomizers;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext context) {
        ApiResponses apiResponses = buildFromMethodAnnotations(operation, context);
        for (OperationApiResponsesCustomizer customizer : operationApiResponsesCustomizers) {
            customizer.customize(apiResponses, context);
        }
        ReferencedApiResponsesConsumer referencedApiResponsesConsumer = context.getReferencedItemConsumerSupplier().get(ReferencedApiResponsesConsumer.class);
        setMapIfNotEmpty(apiResponses, responses -> referencedApiResponsesConsumer.maybeAsReference(responses, operation::setResponses));
    }

    private ApiResponses buildFromMethodAnnotations(Operation operation, OperationBuilderContext context) {
        ApiResponses apiResponses = Optional.ofNullable(operation.getResponses()).orElseGet(ApiResponses::new);

        Method method = context.getOperationInfo().getHandlerMethod().getMethod();

        AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method);
        getMergedApiResponsesFromSupplier(annotationsSupplier).forEach(apiResponseAnnotation -> {
            String responseCode = apiResponseAnnotation.responseCode();
            if (StringUtils.isBlank(responseCode)) {
                // at least it should be set to the "default" string
                throw new IllegalStateException("Encountered ApiResponse annotation with empty response code");
            }
            ApiResponse apiResponse = apiResponses.computeIfAbsent(responseCode, ignored -> new ApiResponse());
            ApiResponse smartImmutableApiResponse = OpenApiProxyUtils.smartImmutableProxy(apiResponse, OpenApiProxyUtils::addNonExistingKeys);
            apiResponseAnnotationMapper.applyFromAnnotation(smartImmutableApiResponse, apiResponseAnnotation, context.getReferencedItemConsumerSupplier());
        });

        return apiResponses;
    }

    private static Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> getMergedApiResponsesFromSupplier(AnnotationsSupplier annotationsSupplier) {
        return Stream.concat(
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.responses.ApiResponses.class).flatMap(x -> Stream.of(x.value())),
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.responses.ApiResponse.class)
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
