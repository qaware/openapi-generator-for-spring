package de.qaware.openapigeneratorforspring.model.info;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * Contact
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#contactObject"
 */
@Data
public class Contact implements HasExtensions {
    private String name;
    private String url;
    private String email;
    private Map<String, Object> extensions;
}
