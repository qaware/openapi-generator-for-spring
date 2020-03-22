package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MethodResponseApiResponseCustomizer implements OperationApiResponseCustomizer, Ordered {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    private final DefaultApiResponseCodeMapper defaultApiResponseCodeMapper;
    private final SchemaResolver schemaResolver;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        String responseCodeFromMethod = defaultApiResponseCodeMapper.getResponseCodeFromMethod(method);
        ApiResponse defaultApiResponse = apiResponses.computeIfAbsent(responseCodeFromMethod, ignored -> new ApiResponse());

        if (StringUtils.isBlank(defaultApiResponse.getDescription())) {
            // TODO make this description customizable?
            defaultApiResponse.setDescription("Default response");
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
            Schema schema = schemaResolver.resolveFromType(method.getGenericReturnType(), annotationsSupplier, operationBuilderContext.getReferencedSchemaConsumer());
            mediaType.setSchema(schema);
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
        RequestMapping requestMappingAnnotation = annotationsSupplierFromMethodWithDeclaringClass.findFirstAnnotation(RequestMapping.class);
        if (requestMappingAnnotation == null || ArrayUtils.isEmpty(requestMappingAnnotation.produces())) {
            return Collections.singletonList(org.springframework.http.MediaType.ALL_VALUE);
        }
        return Arrays.asList(requestMappingAnnotation.produces());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
