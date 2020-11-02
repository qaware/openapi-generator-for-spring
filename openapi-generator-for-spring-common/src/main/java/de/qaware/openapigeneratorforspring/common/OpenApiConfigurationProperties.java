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
    private boolean enabled = true;
    private String apiDocsPath = OPEN_API_DOCS_DEFAULT_PATH;
    private Server server = new Server();

    @Getter
    @Setter
    public static class Server {
        private boolean addDefault = true;
    }

    public static class EnabledCondition extends ConfigurationPropertyCondition<OpenApiConfigurationProperties> {
        public EnabledCondition() {
            super(OpenApiConfigurationProperties.class, OpenApiConfigurationProperties::isEnabled);
        }
    }
}
