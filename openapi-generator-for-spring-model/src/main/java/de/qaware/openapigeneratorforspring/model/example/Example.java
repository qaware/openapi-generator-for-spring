package de.qaware.openapigeneratorforspring.model.example;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * Example
 */
@Data
public class Example implements HasExtensions {
    private String summary;
    private String description;
    private Object value;
    private String externalValue;
    private String $ref;
    private Map<String, Object> extensions;
}
