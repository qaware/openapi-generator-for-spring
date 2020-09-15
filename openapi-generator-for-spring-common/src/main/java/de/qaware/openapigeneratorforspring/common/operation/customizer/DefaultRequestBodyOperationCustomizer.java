package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultRequestBodyOperationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setRequestBody(buildRequestBodyFromMethod(operationBuilderContext), operation);
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        RequestBody requestBody = buildRequestBodyFromMethod(operationBuilderContext);
        requestBodyAnnotationMapper.applyFromAnnotation(requestBody, operationAnnotation.requestBody(),
                operationBuilderContext.getReferencedItemConsumerSupplier()
        );
        setRequestBody(requestBody, operation);
    }

    private void setRequestBody(RequestBody requestBody, Operation operation) {
        if (new RequestBody().equals(requestBody)) {
            return;
        }
        // TODO add referenced item consumer for request body
        operation.setRequestBody(requestBody);
    }

    private RequestBody buildRequestBodyFromMethod(OperationBuilderContext operationBuilderContext) {
        OperationInfo operationInfo = operationBuilderContext.getOperationInfo();
        ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class);
        return Stream.of(operationInfo.getHandlerMethod().getMethod().getParameters())
                .flatMap(
                        methodParameter -> {
                            AnnotationsSupplier annotationsSupplierFromMethodParameter = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter);
                            return annotationsSupplierFromMethodParameter.findAnnotations(org.springframework.web.bind.annotation.RequestBody.class)
                                    .findFirst() // should not happen to find multiple annotations on parameter
                                    .map(requestBodyAnnotation -> {
                                        RequestBody requestBody = new RequestBody();
                                        // TODO check @Nullable on method parameter?
                                        requestBody.setRequired(requestBodyAnnotation.required());
                                        Content content = getConsumesContentType(operationInfo).stream().collect(Collectors.toMap(
                                                x -> x,
                                                contentType -> {
                                                    MediaType mediaType = new MediaType();
                                                    schemaResolver.resolveFromType(methodParameter.getType(),
                                                            // TODO check annotations supplier building here
                                                            annotationsSupplierFromMethodParameter,
                                                            referencedSchemaConsumer,
                                                            mediaType::setSchema
                                                    );
                                                    return mediaType;
                                                },
                                                (a, b) -> {
                                                    throw new IllegalStateException("Found conflicting content types: " + a + " vs. " + b);
                                                },
                                                Content::new
                                        ));
                                        setMapIfNotEmpty(content, requestBody::setContent);
                                        return requestBody;
                                    })
                                    .map(Stream::of).orElse(Stream.empty()); // Optional.toStream()
                        }
                )
                .reduce((a, b) -> {
                    // TODO check if multiple usages of @RequestBody are allowed by Spring Web
                    throw new IllegalStateException("Found more than one @RequestBody annotation on " + operationInfo);
                })
                .orElseGet(RequestBody::new);
    }

    private List<String> getConsumesContentType(OperationInfo operationInfo) {
        AnnotationsSupplier annotationsSupplierFromMethodWithDeclaringClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationInfo.getHandlerMethod().getMethod());
        RequestMapping requestMappingAnnotation = annotationsSupplierFromMethodWithDeclaringClass.findFirstAnnotation(RequestMapping.class);
        if (requestMappingAnnotation == null || ArrayUtils.isEmpty(requestMappingAnnotation.consumes())) {
            return Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE);
        }
        return Arrays.asList(requestMappingAnnotation.consumes());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
