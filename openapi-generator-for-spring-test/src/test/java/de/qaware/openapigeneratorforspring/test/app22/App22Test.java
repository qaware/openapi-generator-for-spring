package de.qaware.openapigeneratorforspring.test.app22;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.api-docs-path=/my/own-path/to-api-docs")
public class App22Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testWithCustomApiDocsPath() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app22.json",
                webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/my/own-path/to-api-docs")
                        .build()
                )
        );
    }
}
