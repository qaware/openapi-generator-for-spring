package de.qaware.openapigeneratorforspring.model.security;

import com.fasterxml.jackson.annotation.JsonValue;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * SecurityScheme
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#securitySchemeObject"
 */
@Data
public class SecurityScheme implements HasExtensions {

    private Type type;
    private String description;
    private String name;
    private String $ref;
    private In in;
    private String scheme;
    private String bearerFormat;
    private OAuthFlows flows;
    private String openIdConnectUrl;
    private Map<String, Object> extensions;

    @RequiredArgsConstructor
    public enum Type {
        APIKEY("apiKey"),
        HTTP("http"),
        OAUTH2("oauth2"),
        OPENIDCONNECT("openIdConnect");

        @JsonValue
        @Getter
        private final String value;
    }

    @RequiredArgsConstructor
    public enum In {
        COOKIE("cookie"),
        HEADER("header"),
        QUERY("query");

        @JsonValue
        @Getter
        private final String value;
    }
}

