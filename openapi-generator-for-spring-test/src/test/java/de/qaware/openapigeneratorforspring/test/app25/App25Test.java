package de.qaware.openapigeneratorforspring.test.app25;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.ui.enabled=false")
public class App25Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testSwaggerUiIsNotFoundWhenDisabled() throws Exception {
        webTestClient.get().uri(uriBuilder -> uriBuilder
                .path("/v3/api-docs")
                .build())
                .exchange()
                .expectStatus().isOk();
        webTestClient.get().uri(uriBuilder -> uriBuilder
                .path("/swagger-ui/index.html")
                .build())
                .exchange()
                .expectStatus().isNotFound();
    }
}
