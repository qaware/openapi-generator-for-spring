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
     * Flag if exposing Swagger UI is enabled.
     */
    private boolean enabled = true;
    /**
     * Path to the Swagger UI.
     */
    private String path = "/swagger-ui";

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiSwaggerUiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiSwaggerUiConfigurationProperties.class, OpenApiSwaggerUiConfigurationProperties::isEnabled);
        }
    }
}
