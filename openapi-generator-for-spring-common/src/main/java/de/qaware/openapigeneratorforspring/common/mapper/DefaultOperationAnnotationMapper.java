package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultOperationAnnotationMapper implements OperationAnnotationMapper {

    private final RequestBodyAnnotationMapper requestBodyAnnotationMapper;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;


    @Override
    public void applyFromAnnotation(Operation operation, io.swagger.v3.oas.annotations.Operation operationAnnotation,
                                    ReferencedItemConsumerSupplier referencedItemConsumerSupplier,
                                    Consumer<List<Tag>> tagsConsumer) {
        setStringIfNotBlank(operationAnnotation.operationId(), operation::setOperationId);
        setStringIfNotBlank(operationAnnotation.summary(), operation::setSummary);
        setStringIfNotBlank(operationAnnotation.description(), operation::setDescription);
        setRequestBody(operation, operationAnnotation, referencedItemConsumerSupplier);

        if (operationAnnotation.deprecated()) {
            operation.setDeprecated(true);
        }

        setTags(operation, operationAnnotation.tags(), tagsConsumer);

        setCollectionIfNotEmpty(serverAnnotationMapper.mapArray(operationAnnotation.servers()), operation::setServers);
        externalDocumentationAnnotationMapper.map(operationAnnotation.externalDocs())
                .ifPresent(operation::setExternalDocs);
        setMapIfNotEmpty(
                extensionAnnotationMapper.mapArray(operationAnnotation.extensions()),
                operation::setExtensions
        );
    }

    private void setRequestBody(Operation operation, io.swagger.v3.oas.annotations.Operation operationAnnotation, ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        RequestBody requestBody = requestBodyAnnotationMapper.buildFromAnnotation(operationAnnotation.requestBody(), referencedItemConsumerSupplier);
        if (!requestBody.isEmpty()) {
            operation.setRequestBody(requestBody);
        }
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
