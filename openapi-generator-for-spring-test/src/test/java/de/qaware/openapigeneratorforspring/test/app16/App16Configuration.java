package de.qaware.openapigeneratorforspring.test.app16;

import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import de.qaware.openapigeneratorforspring.model.server.Server;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class App16Configuration {

    @Bean
    public OpenApiServersSupplier openApiServersSupplier() {
        return () -> Collections.singletonList(
                Server.builder()
                        .url("http://some-url-from-supplier")
                        .description("Description from supplier")
                        .build()
        );
    }

    @Bean
    public OpenApiSecuritySchemesSupplier openApiSecuritySchemesSupplier() {
        return () -> Collections.singletonMap(
                "scheme2",
                SecurityScheme.builder()
                        .type(SecuritySchemeType.HTTP.toString())
                        .description("scheme from supplier")
                        .in(SecuritySchemeIn.COOKIE.toString())
                        .build()
        );
    }
}
