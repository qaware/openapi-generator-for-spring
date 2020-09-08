package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkAnnotationMapper linkAnnotationMapper;
    private final ApiResponseCodeMapper apiResponseCodeMapper;
    private final List<OperationApiResponseCustomizer> operationApiResponseCustomizers;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        fillApiResponses(operation, getApiResponseAnnotationsFromMethod(method), operationBuilderContext);
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        // put the annotations from the operation last, which gives them the highest precedence
        Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations
                = Stream.concat(getApiResponseAnnotationsFromMethod(method), Stream.of(operationAnnotation.responses()));
        fillApiResponses(operation, apiResponseAnnotations, operationBuilderContext);
    }

    private void fillApiResponses(Operation operation, Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations, OperationBuilderContext operationBuilderContext) {
        ApiResponses apiResponses = buildApiResponsesFromAnnotations(apiResponseAnnotations, operationBuilderContext);
        for (OperationApiResponseCustomizer customizer : operationApiResponseCustomizers) {
            customizer.customize(apiResponses, operationBuilderContext);
        }
        setMapIfNotEmpty(apiResponses, operation::setResponses);
    }


    private Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> getApiResponseAnnotationsFromMethod(Method method) {
        AnnotationsSupplier annotationsSupplierFromMethod = annotationsSupplierFactory.createFromAnnotatedElement(method);
        AnnotationsSupplier annotationsSupplierFromClass = annotationsSupplierFactory.createFromAnnotatedElement(method.getDeclaringClass());

        // first the annotations from declaring class,
        // then the annotations from methods
        // this way it's possible to overwrite responses on method level
        return Stream.concat(
                getMergedApiResponsesFromSupplier(annotationsSupplierFromClass),
                getMergedApiResponsesFromSupplier(annotationsSupplierFromMethod)
        );
    }

    private Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> getMergedApiResponsesFromSupplier(AnnotationsSupplier annotationsSupplier) {
        return Stream.concat(
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.responses.ApiResponses.class).flatMap(x -> Stream.of(x.value())),
                annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.responses.ApiResponse.class)
        );
    }

    private ApiResponses buildApiResponsesFromAnnotations(
            Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations,
            OperationBuilderContext operationBuilderContext
    ) {
        ApiResponses apiResponses = new ApiResponses();
        apiResponseAnnotations.forEachOrdered(annotation -> {
            String responseCode = apiResponseCodeMapper.map(annotation, operationBuilderContext);
            ApiResponse apiResponse = apiResponses.computeIfAbsent(responseCode, ignored -> new ApiResponse());
            setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
            mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headerAnnotationMapper.mapArray(annotation.headers(), operationBuilderContext.getReferencedSchemaConsumer()));
            mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, linkAnnotationMapper.mapArray(annotation.links()));
            mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content(), operationBuilderContext.getReferencedSchemaConsumer()));
            mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
            setStringIfNotBlank(annotation.ref(), apiResponse::set$ref);
        });
        return apiResponses;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
