package de.qaware.openapigeneratorforspring.model.security;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * SecurityRequirement
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#securityRequirementObject"
 */
public class SecurityRequirement extends LinkedHashMap<String, List<String>> {

    public SecurityRequirement requirement(String securitySchemeName, String... scopes) {
        put(securitySchemeName, Arrays.asList(scopes));
        return this;
    }

}

