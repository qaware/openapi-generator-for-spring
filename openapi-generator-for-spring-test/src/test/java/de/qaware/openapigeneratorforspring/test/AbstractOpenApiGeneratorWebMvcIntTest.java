package de.qaware.openapigeneratorforspring.test;

import org.junit.Test;

public abstract class AbstractOpenApiGeneratorWebMvcIntTest extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorWebMvcIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, performApiDocsRequest(x -> x));
    }
}
