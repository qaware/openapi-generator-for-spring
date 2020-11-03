package de.qaware.openapigeneratorforspring.test.app21;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcBaseIntTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "openapi-generator-for-spring.api-docs-path=/my/own-path/to-api-docs",
        "openapi-generator-for-spring.ui.path=/my/own-path/to-swagger-ui",
        "server.servlet.context-path=/my-context-path",
        "server.forward-headers-strategy = framework"
})
public class App21Test extends AbstractOpenApiGeneratorWebMvcBaseIntTest {

    @Autowired
    private OpenApiConfigurationProperties properties;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int serverPort;

    @Test
    public void testWithCustomApiDocsPath() throws Exception {
        assertResponseBodyMatchesOpenApiJson("app21.json", () ->
                getResponseBody("/my/own-path/to-api-docs").replace("http://localhost:" + serverPort, "http://localhost")
        );
    }

    @Test
    public void testSwaggerUiIndexHtmlWithCustomPath() throws Exception {
        String responseBody = getResponseBody("/my/own-path/to-swagger-ui/index.html");
        assertThat(responseBody).contains(buildUrl("/my/own-path/to-api-docs"));
    }

    @Test
    public void testSwaggerUiRedirectToIndexHtml() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("x-forwarded-host", singletonList("forwarded-host"));
        httpHeaders.put("x-forwarded-port", singletonList("1337"));
        httpHeaders.put("x-forwarded-proto", singletonList("https"));
        assertRedirectEntity(getResponseEntity("/my/own-path/to-swagger-ui", httpHeaders));
        assertRedirectEntity(getResponseEntity("/my/own-path/to-swagger-ui/", httpHeaders));
    }

    private void assertRedirectEntity(ResponseEntity<String> redirectEntity1) {
        assertThat(redirectEntity1.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(redirectEntity1.getHeaders()).contains(entry("Location",
                singletonList("https://forwarded-host:1337/my-context-path/my/own-path/to-swagger-ui/index.html")
        ));
    }

    @Test
    public void testConfigurationPropertyIsSet() throws Exception {
        // we're using SPEL in the controller's @RequestMapping,
        // so make sure it's also working with configuration properties
        assertThat(properties.getApiDocsPath()).isEqualTo("/my/own-path/to-api-docs");
    }

    private String getResponseBody(String path) {
        ResponseEntity<String> entity = getResponseEntity(path);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        return entity.getBody();
    }

    private ResponseEntity<String> getResponseEntity(String path) {
        return getResponseEntity(path, new HttpHeaders());
    }

    private ResponseEntity<String> getResponseEntity(String path, HttpHeaders requestHeaders) {
        HttpEntity<Void> entity = new HttpEntity<>(null, requestHeaders);
        return testRestTemplate.exchange(buildUrl(path), HttpMethod.GET, entity, String.class);
    }

    protected String buildUrl(String path) {
        return "http://localhost:" + serverPort + "/my-context-path" + path;
    }

}
