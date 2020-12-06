package de.qaware.openapigeneratorforspring.test.app23;

import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebFluxBaseIntTest;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "openapi-generator-for-spring.enabled=false")
public class App23Test extends AbstractOpenApiGeneratorWebFluxBaseIntTest {

    @Test
    public void testOpenApiIsNotFoundWhenDisabled() {
        performApiDocsRequest(x -> x, x -> x).expectStatus().isNotFound();
    }
}
