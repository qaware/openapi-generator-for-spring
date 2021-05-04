package de.qaware.openapigeneratorforspring.test.app40;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "spring.main.web-application-type = REACTIVE")
class App40Test extends AbstractOpenApiGeneratorWebIntTest {

    @Test
    void testSwaggerUiIndexHtml() {
        assertThat(getResponseBody("/swagger-ui/index.html")).doesNotContain(CSRF_TOKEN_HEADER_NAME);
    }
}
