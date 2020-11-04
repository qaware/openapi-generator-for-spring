package de.qaware.openapigeneratorforspring.test.app12;

import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPostFilter;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterPreFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class App12Configuration {

    public static final String X_FILTERED_HEADER_NAME = "X-Filtered-Header-Name";

    @Bean
    public OperationParameterPreFilter ignoreSpecificHeaderParameterFilter() {
        return methodParameter -> Optional.ofNullable(methodParameter.getAnnotationsSupplier().findFirstAnnotation(RequestHeader.class))
                .map(annotation -> !annotation.value().equals(X_FILTERED_HEADER_NAME))
                .orElse(true);
    }

    @Bean
    public OperationParameterPostFilter ignoredSpecificParameterName() {
        return parameter -> !Objects.equals(parameter.getName(), "filteredParameterName");
    }
}
