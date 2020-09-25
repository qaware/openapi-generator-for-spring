package de.qaware.openapigeneratorforspring.test;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
public abstract class AbstractOpenApiGeneratorIntTest {

    private static final String RESOURCE_PATH_PREFIX = "/openApiJson/";

    private final String expectedJsonFile;

    protected AbstractOpenApiGeneratorIntTest(String expectedJsonFile) {
        this.expectedJsonFile = expectedJsonFile;
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    public void getOpenApiAsJson() throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, webTestClient.get().uri("/v3/api-docs"));
    }

    public static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, WebTestClient.RequestHeadersSpec<?> performResult) throws Exception {
        String expectedJson = readFileAsString(RESOURCE_PATH_PREFIX + expectedJsonFile);
        byte[] responseBody = performResult
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        JSONAssert.assertEquals(expectedJson, new String(responseBody), true);
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
