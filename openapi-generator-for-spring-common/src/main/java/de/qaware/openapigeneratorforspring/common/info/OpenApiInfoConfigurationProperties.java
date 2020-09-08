package de.qaware.openapigeneratorforspring.common.info;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nullable;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiConstants.CONFIG_PROPERTIES_PREFIX;

@ConfigurationProperties(CONFIG_PROPERTIES_PREFIX + ".info")
@Getter
@Setter
public class OpenApiInfoConfigurationProperties {
    @Nullable
    private String title;
    @Nullable
    private String description;
    @Nullable
    private String termsOfService;
    @Nullable
    private Contact contact;
    @Nullable
    private License license;
    @Nullable
    private String version;
    @Nullable
    private Map<String, Object> extensions;

    @Getter
    @Setter
    public static class Contact {
        @Nullable
        private String name;
        @Nullable
        private String url;
        @Nullable
        private String email;
        @Nullable
        private Map<String, Object> extensions;
    }

    @Getter
    @Setter
    public static class License {
        @Nullable
        private String name;
        @Nullable
        private String url;
        @Nullable
        private Map<String, Object> extensions;
    }
}
