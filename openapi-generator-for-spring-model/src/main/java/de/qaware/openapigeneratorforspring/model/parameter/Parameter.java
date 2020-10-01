package de.qaware.openapigeneratorforspring.model.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.reference.HasReference;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Parameter
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#parameterObject"
 */
@Data
@Builder
public class Parameter implements HasExtensions, HasReference {
    private String name;
    private String in;
    private String description;
    private Boolean required;
    private Boolean deprecated;
    private Boolean allowEmptyValue;
    private String ref;

    private String style;
    private Boolean explode;
    private Boolean allowReserved;
    private Schema schema;
    private Map<String, Example> examples;
    private Object example;
    private Content content;
    private Map<String, Object> extensions;

    @JsonIgnore
    public boolean isEmpty() {
        return builder().build().equals(this);
    }
}

