package de.qaware.openapigeneratorforspring.model.example;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import de.qaware.openapigeneratorforspring.model.trait.HasReference;
import lombok.Data;

import java.util.Map;

/**
 * Example
 */
@Data
public class Example implements HasExtensions, HasReference<Example>, HasIsEmpty<Example> {
    private String summary;
    private String description;
    private Object value;
    private String externalValue;
    private String ref;
    private Map<String, Object> extensions;

    @Override
    public Example createInstance() {
        return new Example();
    }
}
