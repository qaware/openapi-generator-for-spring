package de.qaware.openapigeneratorforspring.test;


import org.junit.jupiter.api.Test;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonIntegrationTestUtils.findResourceFilenameFromTestClass;

public abstract class AbstractOpenApiGeneratorWebFluxIntTest extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    private final String expectedJsonFile = findResourceFilenameFromTestClass(getClass(), ".json");

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, performApiDocsRequest(x -> x, x -> x));
    }
}
