package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.util.OpenApiBaseUriProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.emptyListIfNull;

@RequiredArgsConstructor
public class OpenApiRequestAwareProviderForWebMvc implements OpenApiRequestParameterProvider, OpenApiBaseUriProvider {

    private final WebRequest webRequest;
    private final HttpServletRequest httpServletRequest;

    @Override
    public String getBaseUri() {
        // works because we're in request scope here
        return ServletUriComponentsBuilder.fromContextPath(httpServletRequest).toUriString();
    }

    @Override
    public List<String> getHeaderValues(String headerName) {
        return emptyListIfNull(webRequest.getHeaderValues(headerName));
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return emptyListIfNull(webRequest.getParameterValues(parameterName));
    }
}
