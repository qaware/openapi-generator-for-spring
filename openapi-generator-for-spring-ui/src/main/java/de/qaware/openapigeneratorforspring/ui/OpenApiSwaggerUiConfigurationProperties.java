package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConfigurationPropertiesUtil;
import de.qaware.openapigeneratorforspring.common.util.OpenApiConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OpenApiConstants.CONFIG_PROPERTIES_PREFIX + ".ui")
@Getter
@Setter
public class OpenApiSwaggerUiConfigurationProperties {
    private boolean enabled = true;
    private String path = "/swagger-ui";

    public static class EnabledCondition extends OpenApiConfigurationPropertiesUtil.ConfigurationPropertyCondition<OpenApiSwaggerUiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiSwaggerUiConfigurationProperties.class, OpenApiSwaggerUiConfigurationProperties::isEnabled);
        }
    }
}
