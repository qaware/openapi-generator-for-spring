package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumerSupplier;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultRequestBodyAnnotationMapper implements RequestBodyAnnotationMapper {
    private final ContentAnnotationMapper contentAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public void applyFromAnnotation(RequestBody requestBody, io.swagger.v3.oas.annotations.parameters.RequestBody annotation,
                                    ReferencedItemConsumerSupplier referencedItemConsumerSupplier) {
        setStringIfNotBlank(annotation.description(), requestBody::setDescription);
        setMapIfNotEmpty(contentAnnotationMapper.mapArray(annotation.content(), referencedItemConsumerSupplier), requestBody::setContent);
        if (annotation.required()) {
            requestBody.setRequired(true);
        }
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), requestBody::setExtensions);
        // TODO think about handling explicit ref from annotation, could be used as an suggested identifier?
    }
}
