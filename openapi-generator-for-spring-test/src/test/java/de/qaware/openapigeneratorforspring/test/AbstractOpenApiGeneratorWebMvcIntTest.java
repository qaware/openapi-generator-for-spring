package de.qaware.openapigeneratorforspring.test;


import org.junit.jupiter.api.Test;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonIntegrationTestUtils.findResourceFilenameFromTestClass;

public abstract class AbstractOpenApiGeneratorWebMvcIntTest extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    private final String expectedJsonFile = findResourceFilenameFromTestClass(getClass(), ".json");

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, performApiDocsRequest(x -> x));
    }
}
