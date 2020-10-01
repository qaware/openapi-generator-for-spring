package de.qaware.openapigeneratorforspring.model.info;

import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * License
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#licenseObject"
 */
@Data
public class License implements HasExtensions {
    private String name;
    private String url;
    private Map<String, Object> extensions;
}

