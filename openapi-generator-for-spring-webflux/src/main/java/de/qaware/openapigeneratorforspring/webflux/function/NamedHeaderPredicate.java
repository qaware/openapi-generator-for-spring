package de.qaware.openapigeneratorforspring.webflux.function;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.ServerRequest;

@RequiredArgsConstructor(staticName = "of")
public class NamedHeaderPredicate implements RequestPredicate {

    private final String headerName;
    private final String headerValue;

    @Override
    public boolean test(ServerRequest request) {
        if (CorsUtils.isPreFlightRequest(request.exchange().getRequest())) {
            return true;
        } else {
            return request.headers().header(headerName).contains(headerValue);
        }
    }

    @Override
    public void accept(RequestPredicates.Visitor visitor) {
        visitor.header(headerName, headerValue);
    }

    @Override
    public String toString() {
        return headerName + ": " + headerValue;
    }
}
