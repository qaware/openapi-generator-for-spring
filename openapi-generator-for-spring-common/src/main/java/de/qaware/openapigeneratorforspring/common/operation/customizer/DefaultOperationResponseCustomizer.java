package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ContentAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.HeaderAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.LinkAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiAnnotationUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer, Ordered {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    private final HeaderAnnotationMapper headerAnnotationMapper;
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkAnnotationMapper linkAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        fillApiResponses(operation, getApiResponseAnnotationsFromMethod(method));
    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        // put the annotations from the operation last, which gives them the highest precedence
        Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations
                = Stream.concat(getApiResponseAnnotationsFromMethod(method), Stream.of(operationAnnotation.responses()));
        fillApiResponses(operation, apiResponseAnnotations);
    }

    private void fillApiResponses(Operation operation, Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations) {
        ApiResponses apiResponses = buildApiResponsesFromAnnotations(apiResponseAnnotations);

        setMapIfNotEmpty(apiResponses, operation::setResponses);
    }

    private String getResponseCodeFromMethod(Method method) {
        ResponseStatus responseStatus = OpenApiAnnotationUtils.findAnnotationOnMethodOrClass(method, ResponseStatus.class);
        if (responseStatus != null) {
            return Integer.toString(responseStatus.code().value());
        }
        return Integer.toString(HttpStatus.OK.value());
    }

    private Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> getApiResponseAnnotationsFromMethod(Method method) {

        Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponsesFromClass = Stream.concat(
                AnnotatedElementUtils.findAllMergedAnnotations(method.getDeclaringClass(), io.swagger.v3.oas.annotations.responses.ApiResponses.class).stream()
                        .flatMap(x -> Stream.of(x.value())),
                AnnotatedElementUtils.findMergedRepeatableAnnotations(method.getDeclaringClass(), io.swagger.v3.oas.annotations.responses.ApiResponse.class).stream()
        );

        Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponsesFromMethod = Stream.concat(
                AnnotatedElementUtils.findAllMergedAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponses.class).stream()
                        .flatMap(x -> Stream.of(x.value())),
                AnnotatedElementUtils.findMergedRepeatableAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponse.class).stream()
        );

        // first the annotations from declaring class,
        // then the annotations from methods
        // this way it's possible to overwrite responses on method level
        return Stream.concat(apiResponsesFromClass, apiResponsesFromMethod);
    }

    private ApiResponses buildApiResponsesFromAnnotations(Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations) {
        ApiResponses apiResponses = new ApiResponses();
        apiResponseAnnotations.forEachOrdered(annotation -> {
            ApiResponse apiResponse = apiResponses.computeIfAbsent(annotation.responseCode(), ignored -> new ApiResponse());
            OpenApiStringUtils.setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
            mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, headerAnnotationMapper.mapArray(annotation.headers()));
            mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, linkAnnotationMapper.mapArray(annotation.links()));
            mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, contentAnnotationMapper.mapArray(annotation.content()));
            mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
            OpenApiStringUtils.setStringIfNotBlank(annotation.ref(), apiResponse::set$ref);
        });
        return apiResponses;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
