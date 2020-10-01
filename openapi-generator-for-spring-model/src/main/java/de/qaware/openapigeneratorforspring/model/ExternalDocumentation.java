package de.qaware.openapigeneratorforspring.model;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * ExternalDocumentation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#externalDocumentationObject"
 */
@Data
public class ExternalDocumentation implements HasExtensions {
    private String description;
    private String url;
    private Map<String, Object> extensions;
}
