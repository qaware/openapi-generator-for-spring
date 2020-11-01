package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPreFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App10Configuration {

    @Bean
    public OperationPreFilter operationPreFilterForGroupsQueryParam(OpenApiRequestParameterProvider parameterProvider) {
        return operationInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstQueryParameter("pathPrefix")
                    .map(pathPrefix -> operationInfo.getPathPattern().startsWith(pathPrefix))
                    .orElse(true);
        };
    }

    @Bean
    public OperationPreFilter operationPreFilterForGroupsHeaderParam(OpenApiRequestParameterProvider parameterProvider) {
        return operationInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstHeaderValue("x-path-prefix")
                    .map(pathPrefix -> operationInfo.getPathPattern().startsWith(pathPrefix))
                    .orElse(true);
        };
    }
}
