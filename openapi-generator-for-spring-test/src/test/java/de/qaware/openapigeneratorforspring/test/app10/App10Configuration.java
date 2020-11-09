package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.filter.operation.OperationPreFilter;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier.ApiDocsUriWithName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Configuration
public class App10Configuration {

    @Bean
    public OperationPreFilter operationPreFilterForGroupsQueryParam(OpenApiRequestParameterProvider parameterProvider) {
        return operationInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstQueryParameter("pathPrefix")
                    .map(pathPrefix -> operationInfo.getPathPattern().startsWith(pathPrefix))
                    .orElse(true); // we have another header-based filter, so don't DENY ALL by default
        };
    }

    @Bean
    public OperationPreFilter operationPreFilterForGroupsHeaderParam(OpenApiRequestParameterProvider parameterProvider) {
        return operationInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstHeaderValue("x-path-prefix")
                    .map(pathPrefix -> operationInfo.getPathPattern().startsWith(pathPrefix))
                    .orElse(true);  // we have another query-based filter, so don't DENY ALL by default
        };
    }

    @Bean
    public OpenApiSwaggerUiApiDocsUrisSupplier openApiSwaggerUiApiDocsUrisSupplier() {
        return apiDocsUri -> {
            UriComponentsBuilder apiDocsUriBuilder = UriComponentsBuilder.fromUri(apiDocsUri).query("pathPrefix={pathPrefix}");
            return Arrays.asList(
                    ApiDocsUriWithName.of("User", apiDocsUriBuilder.build("/user")),
                    ApiDocsUriWithName.of("Admin", apiDocsUriBuilder.build("/admin"))
            );
        };
    }
}
