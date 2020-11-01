package de.qaware.openapigeneratorforspring.common;

import java.util.List;
import java.util.Optional;

public interface OpenApiRequestParameterProvider {

    List<String> getHeaderValues(String headerName);

    default Optional<String> getFirstHeaderValue(String headerName) {
        return getHeaderValues(headerName).stream().findFirst();
    }

    List<String> getQueryParameters(String parameterName);

    default Optional<String> getFirstQueryParameter(String parameterName) {
        return getQueryParameters(parameterName).stream().findFirst();
    }
}
