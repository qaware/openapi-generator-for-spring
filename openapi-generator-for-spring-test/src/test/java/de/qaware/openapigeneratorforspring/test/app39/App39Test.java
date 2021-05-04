package de.qaware.openapigeneratorforspring.test.app39;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebIntTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class App39Test extends AbstractOpenApiGeneratorWebIntTest {

    @Test
    void testSwaggerUiIndexHtml() {
        assertThat(getResponseBody("/swagger-ui/index.html"))
                .doesNotContain(CSRF_TOKEN_HEADER_NAME);
    }
}
