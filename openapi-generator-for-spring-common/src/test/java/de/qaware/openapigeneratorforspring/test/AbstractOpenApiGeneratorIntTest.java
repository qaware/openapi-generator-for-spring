package de.qaware.openapigeneratorforspring.test;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class AbstractOpenApiGeneratorIntTest {

    private static final String RESOURCE_PATH_PREFIX = "/openApiJson/";

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void getOpenApiAsJson() throws Exception {
        String expectedJson = readFileAsString(RESOURCE_PATH_PREFIX + expectedJsonFile);
        MvcResult mvcResult = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = mvcResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    public static String readFileAsString(String path) throws IOException {
        try (InputStream stream = AbstractOpenApiGeneratorIntTest.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Unable to find resource '" + path + "'");
            }
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
    }
}
