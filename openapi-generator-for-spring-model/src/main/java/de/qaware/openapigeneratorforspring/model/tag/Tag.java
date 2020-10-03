package de.qaware.openapigeneratorforspring.model.tag;


import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * Tag
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#tagObject"
 */
@Data
public class Tag implements HasExtensions {
    private String name;
    private String description;
    private ExternalDocumentation externalDocs;
    private Map<String, Object> extensions;
}

