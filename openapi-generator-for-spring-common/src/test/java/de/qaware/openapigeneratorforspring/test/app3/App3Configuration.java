package de.qaware.openapigeneratorforspring.test.app3;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.response.MethodResponseApiResponseCustomizer;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App3Configuration {

    @Bean
    public MethodResponseApiResponseCustomizer methodResponseApiResponseCustomizer() {
        return new NoopMethodResponseApiResponseCustomizer();
    }

    private static class NoopMethodResponseApiResponseCustomizer extends MethodResponseApiResponseCustomizer {

        // TODO make overriding method response customization independent of constructor
        public NoopMethodResponseApiResponseCustomizer() {
            super(null, null, null);
        }

        @Override
        public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
            // do nothing
        }
    }
}
