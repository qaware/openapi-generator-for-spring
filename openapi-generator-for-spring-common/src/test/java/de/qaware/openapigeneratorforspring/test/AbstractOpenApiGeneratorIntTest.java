package de.qaware.openapigeneratorforspring.test;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(AbstractOpenApiGeneratorIntTest.TestConfig.class)
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
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, mockMvc.perform(get("/v3/api-docs")));
    }

    public static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, ResultActions performResult) throws Exception {
        String expectedJson = readFileAsString(RESOURCE_PATH_PREFIX + expectedJsonFile);
        MvcResult mvcResult = performResult
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

    @TestConfiguration
    static class TestConfig {

        // TODO Remove this once WebMVC is also separated for int tests
        @Bean
        public HandlerMethodsProvider handlerMethodsProviderFromWebMvc(RequestMappingHandlerMapping requestMappingHandlerMapping) {
            return () -> requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                    .map(entry -> new HandlerMethodWithInfo(
                            entry.getValue(),
                            entry.getKey().getPatternsCondition().getPatterns(),
                            entry.getKey().getMethodsCondition().getMethods()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
