package de.qaware.openapigeneratorforspring.test;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.server.add-default = false")
public abstract class AbstractOpenApiGeneratorWebFluxIntTest extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorWebFluxIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, webTestClient.get().uri("/v3/api-docs"));
    }
}
