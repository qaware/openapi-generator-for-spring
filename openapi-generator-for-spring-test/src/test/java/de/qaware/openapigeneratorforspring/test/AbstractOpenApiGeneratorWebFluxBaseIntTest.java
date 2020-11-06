package de.qaware.openapigeneratorforspring.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.main.web-application-type = REACTIVE")
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient(timeout = "40000000")
public abstract class AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Autowired
    protected WebTestClient webTestClient;

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, WebTestClient.RequestHeadersSpec<?> performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson(expectedJsonFile, () -> getResponseBodyAsString(performResult));
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
