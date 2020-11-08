package de.qaware.openapigeneratorforspring.model.header;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Header
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#headerObject"
 */
@Data
public class Header implements HasExtensions, HasReference<Header> {
    private String description;
    private Boolean required;
    private Boolean deprecated;
    private String style;
    private Boolean explode;
    private Schema schema;
    private Content content;
    private Map<String, Example> examples;
    private Object example;

    private Map<String, Object> extensions;
    private String ref;

    @Override
    public Header createInstance() {
        return new Header();
    }
}

