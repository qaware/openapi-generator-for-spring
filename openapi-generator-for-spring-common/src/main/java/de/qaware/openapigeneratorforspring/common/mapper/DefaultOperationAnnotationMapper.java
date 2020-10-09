package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.operation.parameter.ParameterAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.parameter.reference.ReferencedParametersConsumer;
import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.common.reference.requestbody.ReferencedRequestBodyConsumer;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultOperationAnnotationMapper implements OperationAnnotationMapper {

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ParameterAnnotationMapper parameterAnnotationMapper;
    private final ApiResponseAnnotationMapper apiResponseAnnotationMapper;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Operation map(io.swagger.v3.oas.annotations.Operation operationAnnotation,
                         ReferencedItemConsumerSupplier referencedItemConsumerSupplier,
                         Consumer<List<Tag>> tagsConsumer) {
        Operation operation = new Operation();
        setTags(operation, operationAnnotation.tags(), tagsConsumer);
        setStringIfNotBlank(operationAnnotation.summary(), operation::setSummary);
        setStringIfNotBlank(operationAnnotation.description(), operation::setDescription);
        setRequestBody(operation::setRequestBody, operationAnnotation.requestBody(), referencedItemConsumerSupplier);
        setIfNotEmpty(externalDocumentationAnnotationMapper.map(operationAnnotation.externalDocs()), operation::setExternalDocs);
        setStringIfNotBlank(operationAnnotation.operationId(), operation::setOperationId);
        setParameters(operation::setParameters, operationAnnotation.parameters(), referencedItemConsumerSupplier);
        setResponses(operation::setResponses, operationAnnotation.responses(), referencedItemConsumerSupplier);

        if (operationAnnotation.deprecated()) {
            operation.setDeprecated(true);
        }

        // TODO add security from annotation

        setCollectionIfNotEmpty(serverAnnotationMapper.mapArray(operationAnnotation.servers()), operation::setServers);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(operationAnnotation.extensions()), operation::setExtensions);

        return operation;
    }

    private void setParameters(Consumer<List<Parameter>> setter, io.swagger.v3.oas.annotations.Parameter[] parameterAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setCollectionIfNotEmpty(
                Arrays.stream(parameterAnnotations)
                        .map(annotation -> parameterAnnotationMapper.buildFromAnnotation(annotation, referencedItemConsumerSupplier))
                        .collect(Collectors.toList()),
                parameters -> referencedItemConsumerSupplier.get(ReferencedParametersConsumer.class).maybeAsReference(parameters, setter)
        );
    }

    private void setResponses(Consumer<ApiResponses> setter, ApiResponse[] apiResponseAnnotations, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setMapIfNotEmpty(
                buildStringMapFromStream(Arrays.stream(apiResponseAnnotations),
                        ApiResponse::responseCode,
                        annotation -> apiResponseAnnotationMapper.buildFromAnnotation(annotation, referencedItemConsumerSupplier),
                        ApiResponses::new
                ),
                apiResponses -> referencedItemConsumerSupplier.get(ReferencedApiResponsesConsumer.class).maybeAsReference(apiResponses, setter)
        );
    }

    private void setRequestBody(Consumer<RequestBody> setter, io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setIfNotEmpty(
                requestBodyAnnotationMapper.buildFromAnnotation(requestBodyAnnotation, referencedItemConsumerSupplier),
                requestBody -> referencedItemConsumerSupplier.get(ReferencedRequestBodyConsumer.class).maybeAsReference(requestBody, setter)
        );
    }

    private static void setTags(Operation operation, String[] tags, Consumer<List<Tag>> tagsConsumer) {
        setCollectionIfNotEmpty(
                Stream.of(tags)
                        .filter(StringUtils::isNotBlank)
                        .distinct()
                        .collect(Collectors.toList()),
                operation::setTags
        );
        // also consume tag names here, even if no description or additional information is present
        // the tags consumer is smart enough to merge tags with identical names
        tagsConsumer.accept(
                Stream.of(tags)
                        .map(tagName -> new Tag().withName(tagName))
                        .collect(Collectors.toList())
        );
    }
}
