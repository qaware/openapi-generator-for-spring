package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.util.OpenApiConfigurationPropertiesUtils.ConfigurationPropertyCondition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.OPEN_API_DOCS_DEFAULT_PATH;

@ConfigurationProperties(prefix = CONFIG_PROPERTIES_PREFIX)
@Getter
@Setter
public class OpenApiConfigurationProperties {
    /**
     * Flag to provide the OpenAPI spec as an accessible endpoint.
     */
    private boolean enabled = true;
    /**
     * Path to OpenAPI spec. Default is <code>/v3/api-docs</code>.
     */
    private String apiDocsPath = OPEN_API_DOCS_DEFAULT_PATH;

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiConfigurationProperties.class, OpenApiConfigurationProperties::isEnabled);
        }
    }
}
