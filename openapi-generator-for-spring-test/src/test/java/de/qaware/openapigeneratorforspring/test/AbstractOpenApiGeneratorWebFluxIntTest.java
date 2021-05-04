package de.qaware.openapigeneratorforspring.test;


import org.junit.jupiter.api.Test;

public abstract class AbstractOpenApiGeneratorWebFluxIntTest extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorWebFluxIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, performApiDocsRequest(x -> x, x -> x));
    }
}
