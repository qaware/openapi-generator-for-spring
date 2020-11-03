package de.qaware.openapigeneratorforspring.test;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public abstract class AbstractOpenApiGeneratorWebMvcIntTest extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorWebMvcIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, mockMvc.perform(get("/v3/api-docs")));
    }
}
