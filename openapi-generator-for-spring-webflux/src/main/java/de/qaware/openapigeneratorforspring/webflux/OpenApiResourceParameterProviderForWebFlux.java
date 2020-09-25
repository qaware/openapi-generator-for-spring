package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static de.qaware.openapigeneratorforspring.webflux.OpenApiResource.SERVER_HTTP_REQUEST_THREAD_LOCAL;

@RequiredArgsConstructor
public class OpenApiResourceParameterProviderForWebFlux implements OpenApiResourceParameterProvider {

    @Override
    public List<String> getHeaderValues(String headerName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getHeaders().getOrEmpty(headerName);
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
