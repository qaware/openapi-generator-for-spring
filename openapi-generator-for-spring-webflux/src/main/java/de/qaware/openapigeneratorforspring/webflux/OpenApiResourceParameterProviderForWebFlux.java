package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class OpenApiResourceParameterProviderForWebFlux implements OpenApiResourceParameterProvider {

    private final ServerHttpRequest serverHttpRequest;

    @Override
    public List<String> getHeaderValues(String headerName) {
        return serverHttpRequest.getHeaders().getOrEmpty(headerName);
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return serverHttpRequest.getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
