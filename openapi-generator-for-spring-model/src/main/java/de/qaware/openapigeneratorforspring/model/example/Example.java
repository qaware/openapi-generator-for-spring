package de.qaware.openapigeneratorforspring.model.example;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.reference.HasReference;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Example
 */
@Data
@Builder
public class Example implements HasExtensions, HasReference {
    private String summary;
    private String description;
    private Object value;
    private String externalValue;
    private String ref;
    private Map<String, Object> extensions;
}
