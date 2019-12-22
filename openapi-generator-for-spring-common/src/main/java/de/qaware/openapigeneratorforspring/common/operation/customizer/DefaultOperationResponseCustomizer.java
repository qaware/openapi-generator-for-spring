package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.SchemaMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiAnnotationUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultOperationResponseCustomizer implements OperationCustomizer, Ordered {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    private final SchemaMapper schemaMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        fillApiResponses(operation, getApiResponseAnnotationsFromMethod(method));
    }

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext,
                          io.swagger.v3.oas.annotations.Operation operationAnnotation) {
        Method method = operationBuilderContext.getHandlerMethod().getMethod();
        Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations
                = Stream.concat(getApiResponseAnnotationsFromMethod(method), Stream.of(operationAnnotation.responses()));
        fillApiResponses(operation, apiResponseAnnotations);
    }

    private void fillApiResponses(Operation operation, Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations) {
        ApiResponses apiResponses = buildApiResponsesFromAnnotations(apiResponseAnnotations);

        operation.setResponses(apiResponses);
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

        return Stream.concat(apiResponsesFromClass, apiResponsesFromMethod);
    }

    private ApiResponses buildApiResponsesFromAnnotations(Stream<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponseAnnotations) {
        ApiResponses apiResponses = new ApiResponses();
        apiResponseAnnotations.forEachOrdered(annotation -> {
            ApiResponse apiResponse = apiResponses.computeIfAbsent(annotation.responseCode(), ignored -> new ApiResponse());
            OpenApiStringUtils.setStringIfNotBlank(annotation.description(), apiResponse::setDescription);
            mergeWithExistingMap(apiResponse::getHeaders, apiResponse::setHeaders, mapHeadersAnnotations(annotation.headers()));
            mergeWithExistingMap(apiResponse::getLinks, apiResponse::setLinks, AnnotationsUtils.getLinks(annotation.links()));
            mergeWithExistingMap(apiResponse::getContent, apiResponse::setContent, buildContentFromAnnotation(annotation));
            mergeWithExistingMap(apiResponse::getExtensions, apiResponse::setExtensions, AnnotationsUtils.getExtensions(annotation.extensions()));
            OpenApiStringUtils.setStringIfNotBlank(annotation.ref(), apiResponse::set$ref);
        });
        return apiResponses;
    }

    private Map<String, Header> mapHeadersAnnotations(io.swagger.v3.oas.annotations.headers.Header[] headers) {
        return buildMapFromArray(
                headers,
                io.swagger.v3.oas.annotations.headers.Header::name,
                this::mapHeaderAnnotation
        );
    }

    private Header mapHeaderAnnotation(io.swagger.v3.oas.annotations.headers.Header headerAnnotation) {
        Header header = new Header();
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.description(), header::setDescription);
        if (headerAnnotation.deprecated()) {
            header.setDeprecated(true);
        }
        if (headerAnnotation.required()) {
            header.setRequired(true);
        }
        header.setSchema(schemaMapper.fromAnnotation(headerAnnotation.schema()));
        OpenApiStringUtils.setStringIfNotBlank(headerAnnotation.ref(), header::set$ref);
        return header;
    }

    private Content buildContentFromAnnotation(io.swagger.v3.oas.annotations.responses.ApiResponse annotation) {
        Content contentFromAnnotation = new Content();
        contentFromAnnotation.putAll(
                buildMapFromArray(
                        annotation.content(),
                        io.swagger.v3.oas.annotations.media.Content::mediaType,
                        this::mapContentAnnotation
                )
        );
        return contentFromAnnotation;
    }

    private MediaType mapContentAnnotation(io.swagger.v3.oas.annotations.media.Content contentAnnotation) {
        MediaType mediaType = new MediaType();

        ExampleObject[] examples = contentAnnotation.examples();
        if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
            AnnotationsUtils.getExample(examples[0], true).ifPresent(mediaType::setExample);
        } else {
            setMapIfNotEmpty(mediaType::setExamples,
                    buildMapFromArray(examples, ExampleObject::name, AnnotationsUtils::getExample)
                            .entrySet().stream()
                            .filter(entry -> entry.getValue().isPresent())
                            .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().get()))
            );
        }

        setMapIfNotEmpty(mediaType::setEncoding,
                buildMapFromArray(contentAnnotation.encoding(), io.swagger.v3.oas.annotations.media.Encoding::name, this::mapEncodingAnnotation)
        );

        mediaType.setSchema(schemaMapper.fromAnnotation(contentAnnotation.schema()));
        setMapIfNotEmpty(mediaType::setExtensions, AnnotationsUtils.getExtensions(contentAnnotation.extensions()));
        return mediaType;
    }

    private Encoding mapEncodingAnnotation(io.swagger.v3.oas.annotations.media.Encoding encodingAnnotation) {
        Encoding encoding = new Encoding();

        OpenApiStringUtils.setStringIfNotBlank(encodingAnnotation.contentType(), encoding::setContentType);
        OpenApiStringUtils.setStringIfNotBlank(encodingAnnotation.style(), style -> encoding.setStyle(Encoding.StyleEnum.valueOf(style)));

        if (encodingAnnotation.explode()) {
            encoding.setExplode(true);
        }
        if (encodingAnnotation.allowReserved()) {
            encoding.setAllowReserved(true);
        }

        setMapIfNotEmpty(encoding::setHeaders, mapHeadersAnnotations(encodingAnnotation.headers()));
        setMapIfNotEmpty(encoding::setExtensions, AnnotationsUtils.getExtensions(encodingAnnotation.extensions()));

        return encoding;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


    private static <M extends Map<K, V>, K, V> void mergeWithExistingMap(Supplier<? extends M> getter, Consumer<? super M> setter, M toBeMerged) {
        M existingMap = getter.get();
        if (existingMap != null) {
            existingMap.putAll(toBeMerged);
        } else {
            setMapIfNotEmpty(setter, toBeMerged);
        }
    }

    private static <M extends Map<K, V>, K, V> void setMapIfNotEmpty(Consumer<? super M> setter, M map) {
        if (!map.isEmpty()) {
            setter.accept(map);
        }
    }

    private static <T, K, V> Map<K, V> buildMapFromArray(
            T[] array,
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper
    ) {
        // TODO handle possible duplicate entries here?
        return Stream.of(array).collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
