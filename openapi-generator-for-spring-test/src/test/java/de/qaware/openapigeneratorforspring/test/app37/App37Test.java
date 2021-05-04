package de.qaware.openapigeneratorforspring.test.app37;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class App37Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    private static final String YAML_FILE = "app37.yaml";

    @Test
    void getOpenApiAsJson_withAcceptTextHtml() throws Exception {
        performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "html")))
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    void getOpenApiAsJson_withAcceptApplicationYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }

    @Test
    void getOpenApiAsJson_withAcceptApplicationXYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }

    @Test
    void getOpenApiAsJson_withAcceptTextYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "yaml")))
        );
    }

    @Test
    void getOpenApiAsJson_withAcceptTextXYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "x-yaml")))
        );
    }

    @Test
    void getOpenApiAsJson_withAcceptTextVndYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "vnd.yaml")))
        );
    }
}
