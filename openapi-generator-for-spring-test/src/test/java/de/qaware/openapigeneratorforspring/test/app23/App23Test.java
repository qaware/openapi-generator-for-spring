package de.qaware.openapigeneratorforspring.test.app23;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.enabled=false")
public class App23Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testOpenApiIsNotFoundWhenDisabled() throws Exception {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                .path("/v3/api-docs")
                .build())
                .exchange()
                .expectStatus().isNotFound();
    }
}
