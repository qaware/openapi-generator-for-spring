package de.qaware.openapigeneratorforspring.ui;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(OpenApiSwaggerUiConfigurationProperties.class)
public class OpenApiSwaggerUiAutoConfiguration {
    static final String INDEX_HTML_FILE = "index.html";
    static final String SWAGGER_UI_WEB_JAR = "swagger-ui";
}
