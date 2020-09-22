package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOperationApiResponsesFromMethodCustomizer implements Ordered, OperationApiResponsesFromMethodCustomizer {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    private final DefaultApiResponseCodeMapper defaultApiResponseCodeMapper;
    private final SchemaResolver schemaResolver;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getOperationInfo().getHandlerMethod().getMethod();
        String responseCodeFromMethod = defaultApiResponseCodeMapper.getResponseCodeFromMethod(method);
        ApiResponse defaultApiResponse = apiResponses.computeIfAbsent(responseCodeFromMethod, ignored -> new ApiResponse());

        if (StringUtils.isBlank(defaultApiResponse.getDescription())) {
            // TODO make this description customizable?
            defaultApiResponse.setDescription("Default response");
        }

        if (Void.TYPE.equals(method.getReturnType()) || Void.class.equals(method.getReturnType())) {
            return;
        }

        Content content = getOrCreateEmptyContent(defaultApiResponse);

        AnnotationsSupplier annotationsSupplierFromMethodWithDeclaringClass = annotationsSupplierFactory.createFromMethodWithDeclaringClass(method);
        List<String> producesContentType = getProducesContentType(annotationsSupplierFromMethodWithDeclaringClass);

        for (String contentType : producesContentType) {
            MediaType mediaType = content.computeIfAbsent(contentType, ignored -> new MediaType());
            // just using resolveFromClass here with method.getReturnType() does not work for generic return types
            // TODO check if supplying annotations from type, method and method's declaring class isn't too much searching
            AnnotationsSupplier annotationsSupplier = annotationsSupplierFactory.createFromAnnotatedElement(method.getReturnType())
                    .andThen(annotationsSupplierFromMethodWithDeclaringClass);
            schemaResolver.resolveFromType(method.getGenericReturnType(), annotationsSupplier,
                    operationBuilderContext.getReferencedItemConsumerSupplier().get(ReferencedSchemaConsumer.class),
                    mediaType::setSchema
            );
        }

    }

    private static Content getOrCreateEmptyContent(ApiResponse apiResponse) {
        if (apiResponse.getContent() != null) {
            return apiResponse.getContent();
        }
        Content content = new Content();
        apiResponse.setContent(content);
        return content;
    }

    private static List<String> getProducesContentType(AnnotationsSupplier annotationsSupplierFromMethodWithDeclaringClass) {
        // TODO check if that logic here correctly mimics the way Spring is treating the "produces" property
        return annotationsSupplierFromMethodWithDeclaringClass
                .findAnnotations(RequestMapping.class)
                .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.produces()))
                .findFirst()
                .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.produces()))
                // fallback to "all value" if nothing has been specified
                .orElse(Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
