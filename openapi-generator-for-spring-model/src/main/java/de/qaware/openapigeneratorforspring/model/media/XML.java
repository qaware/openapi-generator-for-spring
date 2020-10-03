package de.qaware.openapigeneratorforspring.model.media;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * XML
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#xmlObject"
 */
@Data
public class XML implements HasExtensions {
    private String name;
    private String namespace;
    private String prefix;
    private Boolean attribute;
    private Boolean wrapped;
    private Map<String, Object> extensions;
}
