package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultExternalDocumentationAnnotationMapper implements ExternalDocumentationAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Optional<ExternalDocumentation> map(io.swagger.v3.oas.annotations.ExternalDocumentation annotation) {
        ExternalDocumentation externalDocumentation = new ExternalDocumentation();
        boolean isNotEmpty =
                setStringIfNotBlank(annotation.description(), externalDocumentation::setDescription) |
                        setStringIfNotBlank(annotation.url(), externalDocumentation::setUrl) |
                        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), externalDocumentation::setExtensions);
        return isNotEmpty ? Optional.of(externalDocumentation) : Optional.empty();
    }
}
