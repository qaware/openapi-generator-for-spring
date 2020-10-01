package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferencedRequestBodyConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.MediaType;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        setRequestBody(buildRequestBodyFromMethod(operationBuilderContext), operation, operationBuilderContext);
    }

    @Override
    public void customizeWithAnnotationPresent(Operation operation, OperationBuilderContext operationBuilderContext,
                                               io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        RequestBody requestBody = buildRequestBodyFromMethod(operationBuilderContext);
        requestBodyAnnotationMapper.applyFromAnnotation(requestBody, operationAnnotation.requestBody(),
                operationBuilderContext.getReferencedItemConsumerSupplier()
        );
        setRequestBody(requestBody, operation, operationBuilderContext);
    }

    private void setRequestBody(RequestBody requestBody, Operation operation, OperationBuilderContext operationBuilderContext) {
        if (requestBody.isEmpty()) {
            return;
        }
        operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedRequestBodyConsumer.class)
                .maybeAsReference(requestBody, operation::setRequestBody);
    }

    private RequestBody buildRequestBodyFromMethod(OperationBuilderContext operationBuilderContext) {
        OperationInfo operationInfo = operationBuilderContext.getOperationInfo();
        ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class);
        return Stream.of(operationInfo.getHandlerMethod().getMethod().getParameters())
                .flatMap(methodParameter -> {
                    AnnotationsSupplier annotationsSupplierFromMethodParameter = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter);
                    return annotationsSupplierFromMethodParameter.findAnnotations(org.springframework.web.bind.annotation.RequestBody.class)
                            .reduce((a, b) -> {
                                throw new IllegalStateException("Found more than one RequestBody annotation on parameter: " + a + " vs. " + b);
                            })
                            .map(springRequestBodyAnnotation -> {
                                RequestBody requestBody = RequestBody.builder().build();
                                // TODO check @Nullable on method parameter?
                                requestBody.setRequired(springRequestBodyAnnotation.required());
                                Content content = getConsumesContentType(operationInfo).stream().collect(Collectors.toMap(
                                        x -> x,
                                        contentType -> {
                                            MediaType mediaType = new MediaType();
                                            schemaResolver.resolveFromType(methodParameter.getType(),
                                                    // the schema resolver should also consider annotations on the type itself
                                                    // however, annotations being present on the parameter itself should take precedence
                                                    annotationsSupplierFromMethodParameter
                                                            .andThen(annotationsSupplierFactory.createFromAnnotatedElement(methodParameter.getType())),
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

                                // also check if swagger annotation is present, which should take precedence (so apply later)
                                io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation = annotationsSupplierFromMethodParameter.findFirstAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class);
                                if (requestBodyAnnotation != null) {
                                    requestBodyAnnotationMapper.applyFromAnnotation(requestBody, requestBodyAnnotation,
                                            operationBuilderContext.getReferencedItemConsumerSupplier()
                                    );
                                }

                                return requestBody;
                            })
                            .map(Stream::of).orElse(Stream.empty()); // Optional.toStream()
                })
                .reduce((a, b) -> {
                    // TODO check if multiple usages of @RequestBody are allowed by Spring Web
                    throw new IllegalStateException("Found more than one @RequestBody annotation on " + operationInfo);
                })
                .orElseGet(() -> RequestBody.builder().build());
    }

    private List<String> getConsumesContentType(OperationInfo operationInfo) {
        // TODO check if that logic here correctly mimics the way Spring is treating the "consumes" property
        // Spring uses it to conditionally check if that handler method is supposed to accept that request,
        // and we need an information on what is supposed to be sent from the client for that method
        return annotationsSupplierFactory.createFromMethodWithDeclaringClass(operationInfo.getHandlerMethod().getMethod())
                .findAnnotations(RequestMapping.class)
                .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.consumes()))
                .findFirst()
                .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.consumes()))
                .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
