package de.qaware.openapigeneratorforspring.test.app18;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App18Configuration {

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsQueryParam(OpenApiRequestParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstQueryParameter("pathPrefix")
                    .map(group -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(group)))
                    .orElse(true);
        };
    }

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsHeaderParam(OpenApiRequestParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstHeaderValue("x-path-prefix")
                    .map(group -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(group)))
                    .orElse(true);
        };
    }
}
