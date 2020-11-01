package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.util.OpenApiBaseUriProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

import static de.qaware.openapigeneratorforspring.webflux.OpenApiResourceForWebFlux.SERVER_HTTP_REQUEST_THREAD_LOCAL;

@RequiredArgsConstructor
public class OpenApiRequestAwareProviderForWebFlux implements OpenApiRequestParameterProvider, OpenApiBaseUriProvider {

    private final OpenApiConfigurationProperties properties;

    @Override
    public String getBaseUri() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpRequest(SERVER_HTTP_REQUEST_THREAD_LOCAL.get());
        String uriPath = uriBuilder.build().getPath();
        String basePath = uriPath != null && uriPath.endsWith(properties.getApiDocsPath()) ?
                uriPath.substring(0, uriPath.length() - properties.getApiDocsPath().length())
                : null;
        return uriBuilder
                .replacePath(StringUtils.isBlank(basePath) ? "/" : basePath)
                .toUriString();
    }

    @Override
    public List<String> getHeaderValues(String headerName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getHeaders().getOrEmpty(headerName);
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return SERVER_HTTP_REQUEST_THREAD_LOCAL.get().getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
