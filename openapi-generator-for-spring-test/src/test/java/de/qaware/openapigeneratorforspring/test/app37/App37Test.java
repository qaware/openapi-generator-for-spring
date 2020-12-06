package de.qaware.openapigeneratorforspring.test.app37;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class App37Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    private static final String YAML_FILE = "app37.yaml";

    @Test
    public void getOpenApiAsJson_withAcceptTextHtml() throws Exception {
        performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "html")))
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    public void getOpenApiAsJson_withAcceptApplicationYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }

    @Test
    public void getOpenApiAsJson_withAcceptApplicationXYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("application", "yaml")))
        );
    }

    @Test
    public void getOpenApiAsJson_withAcceptTextYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "yaml")))
        );
    }

    @Test
    public void getOpenApiAsJson_withAcceptTextXYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "x-yaml")))
        );
    }

    @Test
    public void getOpenApiAsJson_withAcceptTextVndYaml() throws Exception {
        assertResponseBodyMatchesOpenApiYaml(YAML_FILE,
                performApiDocsRequest(x -> x, x -> x.accept(new MediaType("text", "vnd.yaml")))
        );
    }
}
