package de.qaware.openapigeneratorforspring.test;

import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({
        OpenApiDisabledSpringSecurityTestConfiguration.class
})
public abstract class AbstractOpenApiGeneratorWebIntTest {

    protected static final String CSRF_TOKEN_HEADER_NAME = "X-CSRF-TOKEN";

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int serverPort;

    protected String getResponseBody(String path) {
        ResponseEntity<String> entity = getResponseEntity(path);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        return entity.getBody();
    }

    protected ResponseEntity<String> getResponseEntity(String path) {
        return getResponseEntity(path, new HttpHeaders());
    }

    protected ResponseEntity<String> getResponseEntity(String path, HttpHeaders requestHeaders) {
        HttpEntity<Void> entity = new HttpEntity<>(null, requestHeaders);
        return testRestTemplate.exchange(buildHost() + path, HttpMethod.GET, entity, String.class);
    }

    protected String buildHost() {
        return buildHost("http://localhost:" + serverPort);
    }

    protected String buildHost(String host) {
        return host + getContextPath();
    }

    private String getContextPath() {
        if (serverProperties.getServlet() == null) {
            return "";
        }
        if (StringUtils.isBlank(serverProperties.getServlet().getContextPath())) {
            return "";
        }
        return serverProperties.getServlet().getContextPath();
    }
}
