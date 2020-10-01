package de.qaware.openapigeneratorforspring.test.app3;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App3Configuration {

    @Bean
    public OperationApiResponsesFromMethodCustomizer methodResponseApiResponseCustomizer() {
        return new NoopOperationApiResponsesFromMethodCustomizer();
    }

    private static class NoopOperationApiResponsesFromMethodCustomizer implements OperationApiResponsesFromMethodCustomizer {

        @Override
        public void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext) {
            // do nothing
        }
    }
}
