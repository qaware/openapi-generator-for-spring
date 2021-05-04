package de.qaware.openapigeneratorforspring.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.main.web-application-type = REACTIVE")
@AutoConfigureWebTestClient(timeout = "40000000")
@Import(OpenApiDisabledSpringSecurityTestConfiguration.class)
public abstract class AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Autowired
    protected WebTestClient webTestClient;

    protected static void assertResponseBodyMatchesOpenApiJson(String expectedJsonFile, ResponseSpec performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiJson(expectedJsonFile, () -> getResponseBodyAsString(performResult));
    }

    protected static void assertResponseBodyMatchesOpenApiYaml(String expectedYamlFile, ResponseSpec performResult) throws Exception {
        OpenApiJsonIntegrationTestUtils.assertMatchesOpenApiYaml(expectedYamlFile, () -> getResponseBodyAsString(performResult));
    }

    protected ResponseSpec performApiDocsRequest(
            Function<UriBuilder, UriBuilder> uriModifier,
            Function<RequestHeadersSpec<?>, RequestHeadersSpec<?>> requestModifier
    ) {
        return requestModifier.apply(
                webTestClient.get().uri(
                        uriBuilder -> uriModifier.apply(uriBuilder.path("/v3/api-docs")).build()
                )
        ).exchange();
    }

    protected static String getResponseBodyAsString(ResponseSpec performResult) {
        byte[] responseBody = performResult
                .expectStatus().isOk()
                .expectBody().returnResult()
                .getResponseBody();
        assertThat(responseBody).isNotNull();
        return new String(responseBody);
    }
}
