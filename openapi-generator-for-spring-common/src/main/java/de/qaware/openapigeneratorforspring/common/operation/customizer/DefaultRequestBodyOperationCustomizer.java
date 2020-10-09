package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.RequestBodyAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.OperationInfo;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultRequestBodyOperationCustomizer implements OperationCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final SchemaResolver schemaResolver;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        setIfNotEmpty(
                applyFromMethod(operation.getRequestBody(), operationBuilderContext),
                requestBody -> operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedRequestBodyConsumer.class)
                        .maybeAsReference(requestBody, operation::setRequestBody)
        );
    }

    private RequestBody applyFromMethod(@Nullable RequestBody existingRequestBody, OperationBuilderContext operationBuilderContext) {
        OperationInfo operationInfo = operationBuilderContext.getOperationInfo();
        ReferencedSchemaConsumer referencedSchemaConsumer = operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class);
        return Stream.of(operationInfo.getHandlerMethod().getMethod().getParameters())
                .flatMap(methodParameter -> {
                    AnnotationsSupplier annotationsSupplierFromMethodParameter = annotationsSupplierFactory.createFromAnnotatedElement(methodParameter);
                    return Optional.ofNullable(annotationsSupplierFromMethodParameter.findFirstAnnotation(org.springframework.web.bind.annotation.RequestBody.class))
                            .map(springRequestBodyAnnotation -> {
                                RequestBody requestBody = getRequestBody(existingRequestBody, annotationsSupplierFromMethodParameter, operationBuilderContext.getReferencedItemConsumerSupplier());
                                if (requestBody.getRequired() == null) {
                                    requestBody.setRequired(springRequestBodyAnnotation.required());
                                }
                                for (String contentType : getConsumesContentType(operationInfo)) {
                                    MediaType mediaType = getMediaType(contentType, requestBody);
                                    if (mediaType.getSchema() == null) {
                                        schemaResolver.resolveFromType(methodParameter.getType(),
                                                // the schema resolver should also consider annotations on the type itself
                                                // however, annotations being present on the parameter itself should take precedence
                                                annotationsSupplierFromMethodParameter
                                                        .andThen(annotationsSupplierFactory.createFromAnnotatedElement(methodParameter.getType())),
                                                referencedSchemaConsumer,
                                                mediaType::setSchema
                                        );
                                    }
                                }
                                return requestBody;
                            })
                            .map(Stream::of).orElseGet(Stream::empty); // Optional.toStream()
                })
                .reduce((a, b) -> {
                    // TODO check if multiple usages of @RequestBody are allowed by Spring Web
                    throw new IllegalStateException("Found more than one @RequestBody annotation on " + operationInfo);
                })
                .orElseGet(RequestBody::new);
    }

    private RequestBody getRequestBody(@Nullable RequestBody existingRequestBody, AnnotationsSupplier annotationsSupplierFromMethodParameter, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        return Optional.ofNullable(annotationsSupplierFromMethodParameter.findFirstAnnotation(io.swagger.v3.oas.annotations.parameters.RequestBody.class))
                .map(requestBodyAnnotation -> {
                    if (existingRequestBody == null) {
                        return requestBodyAnnotationMapper.buildFromAnnotation(requestBodyAnnotation, referencedItemConsumerSupplier);
                    } else {
                        requestBodyAnnotationMapper.applyFromAnnotation(existingRequestBody, requestBodyAnnotation, referencedItemConsumerSupplier);
                        return existingRequestBody;
                    }
                })
                .orElseGet(() -> existingRequestBody != null ? existingRequestBody : new RequestBody());
    }

    private static MediaType getMediaType(String contentType, RequestBody requestBody) {
        if (requestBody.getContent() == null) {
            Content content = new Content();
            requestBody.setContent(content);
        }
        return requestBody.getContent().computeIfAbsent(contentType, ignored -> new MediaType());
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
