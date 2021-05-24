package de.qaware.openapigeneratorforspring.test.app16;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        tags = {
                @Tag(name = "tag-1")
        },
        servers = {
                @Server(url = "http://url1", description = "Server 1")
        },
        security = {
                @SecurityRequirement(name = "scheme1", scopes = "scope1")
        },
        externalDocs = @ExternalDocumentation(description = "external doc", url = "http://link-to-external"),
        extensions = @Extension(properties = @ExtensionProperty(name = "x-1", value = "x-2"))
)
@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "scheme1",
        paramName = "Api-Key",
        in = SecuritySchemeIn.HEADER
)
class App16 {
    public static void main(String[] args) {
        SpringApplication.run(App16.class, args);
    }
}
