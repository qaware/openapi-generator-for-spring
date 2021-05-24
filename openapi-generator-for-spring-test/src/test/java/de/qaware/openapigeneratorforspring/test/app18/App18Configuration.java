package de.qaware.openapigeneratorforspring.test.app18;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier;
import de.qaware.openapigeneratorforspring.ui.OpenApiSwaggerUiApiDocsUrisSupplier.ApiDocsUriWithName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Configuration
class App18Configuration {

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsQueryParam(OpenApiRequestParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstQueryParameter("pathPrefix")
                    .map(pathPrefix -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(pathPrefix)))
                    .orElse(true);
        };
    }

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsHeaderParam(OpenApiRequestParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstHeaderValue("x-path-prefix")
                    .map(pathPrefix -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(pathPrefix)))
                    .orElse(true);
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
