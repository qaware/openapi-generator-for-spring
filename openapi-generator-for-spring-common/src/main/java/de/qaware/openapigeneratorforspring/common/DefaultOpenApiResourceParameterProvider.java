package de.qaware.openapigeneratorforspring.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiResourceParameterProvider implements OpenApiResourceParameterProvider {

    // TODO I think this works only with WebMVC, as WebFlux provides something else
    private final HttpServletRequest httpServletRequest;

    @Override
    public List<String> getHeaderValues(String headerName) {
        return Collections.list(httpServletRequest.getHeaders(headerName));
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        UriComponents requestUri = UriComponentsBuilder.fromUriString(httpServletRequest.getRequestURI())
                .query(httpServletRequest.getQueryString())
                .build();
        return requestUri.getQueryParams().getOrDefault(parameterName, Collections.emptyList());
    }
}
