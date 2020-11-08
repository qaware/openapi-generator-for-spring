package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiConstants {
    public static final String CONFIG_PROPERTIES_PREFIX = "openapi-generator-for-spring";
    @SuppressWarnings("java:S1075") // suppress hard coded URL warning
    public static final String OPEN_API_DOCS_DEFAULT_PATH = "/v3/api-docs";
}
