package de.qaware.openapigeneratorforspring.test.app3;

import de.qaware.openapigeneratorforspring.common.operation.response.ApiResponseDefaultProvider;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesDescriptionCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.response.OperationApiResponsesFromMethodCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App3Configuration {

    @Bean
    public OperationApiResponsesFromMethodCustomizer simpleOperationApiResponsesFromMethodCustomizer(ApiResponseDefaultProvider apiResponseDefaultProvider) {
        return (apiResponses, operationBuilderContext) -> apiResponses.put("default", apiResponseDefaultProvider.build("200"));
    }

    @Bean
    public OperationApiResponsesDescriptionCustomizer customOperationApiResponsesDescriptionCustomizer() {
        return (apiResponses, operationBuilderContext) -> apiResponses.forEach((responseCode, apiResponse) -> apiResponse.setDescription("My own description"));
    }
}
