package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiRequestParameterProvider;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.emptyListIfNull;

@RequiredArgsConstructor
public class OpenApiRequestAwareSupplierForWebMvc implements OpenApiRequestParameterProvider, OpenApiBaseUriSupplier {

    private final WebRequest webRequest;
    private final HttpServletRequest httpServletRequest;

    @Override
    public URI getBaseUri() {
        // Accessing request-scoped httpServletRequest works here because we're in request scope
        return ServletUriComponentsBuilder.fromContextPath(httpServletRequest).build().toUri();
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
