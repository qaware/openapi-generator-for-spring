package de.qaware.openapigeneratorforspring.test.app12;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.filter.operation.parameter.OperationParameterFilter;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class App12Configuration {

    public static final String X_FILTERED_HEADER_NAME = "X-Filtered-Header-Name";

    @Bean
    public OperationParameterFilter ignoreSpecificHeaderParameterFilter() {
        return new OperationParameterFilter() {
            @Override
            public boolean preAccept(java.lang.reflect.Parameter methodParameter, AnnotationsSupplier parameterAnnotationsSupplier) {
                return Optional.ofNullable(parameterAnnotationsSupplier.findFirstAnnotation(RequestHeader.class))
                        .map(annotation -> !annotation.value().equals(X_FILTERED_HEADER_NAME))
                        .orElse(true);
            }
        };
    }

    @Bean
    public OperationParameterFilter ignoredSpecificParameterName() {
        return new OperationParameterFilter() {
            @Override
            public boolean postAccept(Parameter parameter) {
                return !Objects.equals(parameter.getName(), "filteredParameterName");
            }
        };
    }
}
