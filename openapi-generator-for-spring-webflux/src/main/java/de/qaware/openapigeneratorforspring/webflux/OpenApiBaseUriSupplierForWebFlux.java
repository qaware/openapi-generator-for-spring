package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.OpenApiConfigurationProperties;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiBaseUriSupplier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static de.qaware.openapigeneratorforspring.webflux.OpenApiResourceForWebFlux.SERVER_HTTP_REQUEST_THREAD_LOCAL;

@RequiredArgsConstructor
public class OpenApiBaseUriSupplierForWebFlux implements OpenApiBaseUriSupplier, WebFilter {

    private static final String ATTRIBUTE_NAME = OpenApiBaseUriSupplierForWebFlux.class.getName();

    private final OpenApiConfigurationProperties properties;

    @Override
    public URI getBaseUri() {
        ServerHttpRequest request = SERVER_HTTP_REQUEST_THREAD_LOCAL.get();
        if (request == null) {
            throw new IllegalStateException("No request present in thread local. Probably accessed outside of OpenApi building? Consider using the static exchange variant.");
        }
        return getBaseUri(request);
    }

    public static URI getBaseUri(ServerWebExchange serverWebExchange) {
        Object attribute = serverWebExchange.getAttribute(ATTRIBUTE_NAME);
        if (attribute instanceof OpenApiBaseUriSupplier) {
            OpenApiBaseUriSupplier openApiBaseUriSupplier = (OpenApiBaseUriSupplier) attribute;
            return openApiBaseUriSupplier.getBaseUri();
        }
        // this happens if the OpenApiBaseUriProviderForWebFluxWebFilter is running later than requesting this access
        throw new IllegalStateException("Cannot find attribute " + ATTRIBUTE_NAME);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getAttributes().put(ATTRIBUTE_NAME, (OpenApiBaseUriSupplier) () -> getBaseUri(exchange.getRequest()));
        return chain.filter(exchange);
    }

    private URI getBaseUri(ServerHttpRequest request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpRequest(request);
        String uriPath = uriBuilder.build().getPath();
        String basePath = uriPath != null && uriPath.endsWith(properties.getApiDocsPath()) ?
                uriPath.substring(0, uriPath.length() - properties.getApiDocsPath().length())
                : null;
        return uriBuilder
                .replacePath(StringUtils.isBlank(basePath) ? "/" : basePath)
                .build()
                .toUri();
    }

}
