package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultTagAnnotationMapper implements TagAnnotationMapper {
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Tag map(io.swagger.v3.oas.annotations.tags.Tag tagAnnotation) {
        Tag tag = new Tag();
        if (StringUtils.isBlank(tagAnnotation.name())) {
            throw new IllegalStateException("Tag annotation must not have blank tag name");
        }
        tag.setName(tagAnnotation.name());
        setStringIfNotBlank(tagAnnotation.description(), tag::setDescription);
        setIfNotEmpty(externalDocumentationAnnotationMapper.map(tagAnnotation.externalDocs()), tag::setExternalDocs);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(tagAnnotation.extensions()), tag::setExtensions);
        return tag;
    }
}
