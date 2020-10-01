package de.qaware.openapigeneratorforspring.common.mapper;


import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;

import java.util.Optional;

public interface ExternalDocumentationAnnotationMapper {

    Optional<ExternalDocumentation> map(io.swagger.v3.oas.annotations.ExternalDocumentation externalDocumentationAnnotation);
}
