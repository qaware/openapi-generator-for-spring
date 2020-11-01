package de.qaware.openapigeneratorforspring.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.OPEN_API_DOCS_DEFAULT_PATH;

@ConfigurationProperties(prefix = CONFIG_PROPERTIES_PREFIX)
@Getter
@Setter
public class OpenApiConfigurationProperties {
    private String apiDocsPath = OPEN_API_DOCS_DEFAULT_PATH;
    private boolean addDefaultServer = true;
}
