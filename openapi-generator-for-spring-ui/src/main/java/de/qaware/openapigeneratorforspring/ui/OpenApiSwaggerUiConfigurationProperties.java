package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConfigurationPropertiesUtils.ConfigurationPropertyCondition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger UI specific properties.
 */
@ConfigurationProperties(prefix = OpenApiConstants.CONFIG_PROPERTIES_PREFIX + ".ui")
@Getter
@Setter
public class OpenApiSwaggerUiConfigurationProperties {
    /**
     * Flag if exposing Swagger UI is enabled. Default true.
     */
    private boolean enabled = true;
    /**
     * Path to the Swagger UI.
     */
    @SuppressWarnings("java:S1075") // suppress hard coded URL warning
    private String path = "/swagger-ui";
    /**
     * Enable caching of the Swagger UI resources.
     */
    private boolean cacheUiResources = false;

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiSwaggerUiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiSwaggerUiConfigurationProperties.class, OpenApiSwaggerUiConfigurationProperties::isEnabled);
        }
    }
}
