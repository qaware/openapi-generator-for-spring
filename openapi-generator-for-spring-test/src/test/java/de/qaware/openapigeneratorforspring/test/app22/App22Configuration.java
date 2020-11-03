package de.qaware.openapigeneratorforspring.test.app22;

import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiDefaultServersSupplier;
import de.qaware.openapigeneratorforspring.model.server.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class App22Configuration {
    @Bean
    public OpenApiDefaultServersSupplier openApiDefaultServersSupplier(OpenApiBaseUriSupplier openApiBaseUriSupplier) {
        return () -> Collections.singletonList(Server.builder()
                .description("Default Server for WebFlux Test")
                .url(openApiBaseUriSupplier.getBaseUri().toString())
                .build()
        );
    }
}
