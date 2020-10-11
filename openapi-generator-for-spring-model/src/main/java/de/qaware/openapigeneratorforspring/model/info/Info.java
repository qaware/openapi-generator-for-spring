package de.qaware.openapigeneratorforspring.model.info;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import lombok.Data;

import java.util.Map;

/**
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#infoObject"
 */
@Data
public class Info implements HasExtensions {
    private String title;
    private String description;
    private String termsOfService;
    private Contact contact;
    private License license;
    private String version;
    private Map<String, Object> extensions;
}

