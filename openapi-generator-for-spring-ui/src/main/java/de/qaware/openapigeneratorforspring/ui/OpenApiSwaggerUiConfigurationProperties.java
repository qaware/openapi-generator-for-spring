package de.qaware.openapigeneratorforspring.ui;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OpenApiConstants.CONFIG_PROPERTIES_PREFIX + ".ui")
@Getter
@Setter
public class OpenApiSwaggerUiConfigurationProperties {
    private boolean enable = true;
    private String path = "/swagger-ui";
}
