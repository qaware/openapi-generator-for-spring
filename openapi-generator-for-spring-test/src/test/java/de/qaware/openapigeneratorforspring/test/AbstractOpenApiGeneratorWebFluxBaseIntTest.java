package de.qaware.openapigeneratorforspring.test;

import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Supplier;

import static de.qaware.openapigeneratorforspring.test.OpenApiJsonFileLoader.readOpenApiJsonFile;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.main.web-application-type = REACTIVE")
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout = "40000000")
public abstract class AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Autowired
    protected WebTestClient webTestClient;

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, WebTestClient.RequestHeadersSpec<?> performResult) throws Exception {
        assertResponseBodyMatchesOpenApiJson(expectedJsonFile, () -> getResponseBodyAsString(performResult));
    }

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, Supplier<String> responseBodySupplier) throws Exception {
        String expectedJson = readOpenApiJsonFile(expectedJsonFile);
        String actualJson = responseBodySupplier.get();
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, true);
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage() + "\n\n Actual JSON: " + actualJson, e);
        }
    }

    protected static String getResponseBodyAsString(WebTestClient.RequestHeadersSpec<?> performResult) {
        byte[] responseBody = performResult
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        return new String(responseBody);
    }
}
