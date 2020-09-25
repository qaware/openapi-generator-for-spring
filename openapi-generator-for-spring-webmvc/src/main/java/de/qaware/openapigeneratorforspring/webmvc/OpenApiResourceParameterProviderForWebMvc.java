package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.OpenApiResourceParameterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.emptyListIfNull;

@RequiredArgsConstructor
public class OpenApiResourceParameterProviderForWebMvc implements OpenApiResourceParameterProvider {

    private final WebRequest webRequest;

    @Override
    public List<String> getHeaderValues(String headerName) {
        return emptyListIfNull(webRequest.getHeaderValues(headerName));
    }

    @Override
    public List<String> getQueryParameters(String parameterName) {
        return emptyListIfNull(webRequest.getParameterValues(parameterName));
    }
}
