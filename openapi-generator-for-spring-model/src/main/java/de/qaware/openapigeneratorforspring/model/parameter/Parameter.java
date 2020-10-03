package de.qaware.openapigeneratorforspring.model.parameter;

import de.qaware.openapigeneratorforspring.model.example.Example;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Parameter
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#parameterObject"
 */
@Data
public class Parameter implements HasExtensions, HasReference<Parameter>, HasIsEmpty<Parameter> {
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

    @Override
    public Parameter createInstance() {
        return new Parameter();
    }
}

