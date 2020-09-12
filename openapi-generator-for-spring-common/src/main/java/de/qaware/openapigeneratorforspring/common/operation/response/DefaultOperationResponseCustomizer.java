package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.customizer.OperationCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesConsumer;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ApiResponseAnnotationMapper apiResponseAnnotationMapper;
    private final List<OperationApiResponsesCustomizer> operationApiResponsesCustomizers;
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

    private void fillApiResponses(Operation operation, Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations,
                                  OperationBuilderContext operationBuilderContext) {
        ApiResponses apiResponses = apiResponseAnnotationMapper.buildApiResponsesFromAnnotations(
                apiResponseAnnotations.collect(Collectors.toList()),
                operationBuilderContext
        );
        for (OperationApiResponsesCustomizer customizer : operationApiResponsesCustomizers) {
            customizer.customize(apiResponses, operationBuilderContext);
        }
        ReferencedApiResponsesConsumer referencedApiResponsesConsumer = operationBuilderContext.getReferencedApiResponsesConsumer();
        setMapIfNotEmpty(apiResponses,
                responses -> referencedApiResponsesConsumer.maybeAsReferences(responses, operation::setResponses)
        );
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

    @Override
    public int getOrder() {
        return ORDER;
    }

}
