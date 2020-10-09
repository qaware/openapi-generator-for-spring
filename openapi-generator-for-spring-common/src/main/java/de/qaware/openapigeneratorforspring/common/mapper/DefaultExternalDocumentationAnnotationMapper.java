package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultExternalDocumentationAnnotationMapper implements ExternalDocumentationAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public ExternalDocumentation map(io.swagger.v3.oas.annotations.ExternalDocumentation annotation) {
        ExternalDocumentation externalDocumentation = new ExternalDocumentation();
        setStringIfNotBlank(annotation.description(), externalDocumentation::setDescription);
        setStringIfNotBlank(annotation.url(), externalDocumentation::setUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), externalDocumentation::setExtensions);
        return externalDocumentation;
    }
}
