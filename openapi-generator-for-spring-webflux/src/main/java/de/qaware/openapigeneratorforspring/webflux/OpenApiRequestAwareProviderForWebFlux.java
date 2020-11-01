package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static de.qaware.openapigeneratorforspring.webflux.OpenApiResourceForWebFlux.SERVER_HTTP_REQUEST_THREAD_LOCAL;

@RequiredArgsConstructor
public class OpenApiRequestAwareProviderForWebFlux implements OpenApiRequestParameterProvider {


    @Override
    public List<String> getHeaderValues(String headerName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getHeaders().getOrEmpty(headerName);
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
