package de.qaware.openapigeneratorforspring.test.app10;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import de.qaware.openapigeneratorforspring.common.filter.handlermethod.HandlerMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class App10Configuration {

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsQueryParam(OpenApiResourceParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstQueryParameter("pathPrefix")
                    .map(group -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(group)))
                    .orElse(true);
        };
    }

    @Bean
    public HandlerMethodFilter handlerMethodFilterForGroupsHeaderParam(OpenApiResourceParameterProvider parameterProvider) {
        return handlerMethodWithInfo -> {
            // important to get group inside the lambda, otherwise we're outside of request scope
            return parameterProvider.getFirstHeaderValue("x-path-prefix")
                    .map(group -> handlerMethodWithInfo.getPathPatterns().stream().allMatch(path -> path.startsWith(group)))
                    .orElse(true);
        };
    }
}
