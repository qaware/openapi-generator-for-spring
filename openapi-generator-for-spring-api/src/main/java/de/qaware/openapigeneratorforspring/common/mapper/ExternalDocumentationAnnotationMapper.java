package de.qaware.openapigeneratorforspring.common.mapper;


import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;

@FunctionalInterface
public interface ExternalDocumentationAnnotationMapper {
    ExternalDocumentation map(io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentationAnnotation);
}
