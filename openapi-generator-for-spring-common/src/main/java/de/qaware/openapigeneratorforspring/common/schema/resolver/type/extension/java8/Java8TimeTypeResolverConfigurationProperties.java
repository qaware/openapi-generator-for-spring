package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.java8;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;

@ConfigurationProperties(CONFIG_PROPERTIES_PREFIX + ".extension.java8.time")
@Getter
@Setter
public class Java8TimeTypeResolverConfigurationProperties {

    /**
     * Format to use in schema type.
     */
    private Format format = Format.INFER_FROM_OBJECT_MAPPER;

    @RequiredArgsConstructor
    public enum Format {
        INFER_FROM_OBJECT_MAPPER,
        ISO8601,
        UNIX_EPOCH_SECONDS;
    }
}
