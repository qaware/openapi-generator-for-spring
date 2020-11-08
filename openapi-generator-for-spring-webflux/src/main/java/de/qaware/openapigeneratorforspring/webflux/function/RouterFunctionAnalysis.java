package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.path.RequestMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
class RouterFunctionAnalysis implements RouterFunctions.Visitor, RequestPredicates.Visitor {

    @Getter
    private final Result result = new Result();

    static RouterFunctionAnalysis.Result analyze(RouterFunction<?> routerFunction) {
        RouterFunctionAnalysis visitor = new RouterFunctionAnalysis();
        routerFunction.accept(visitor);
        return visitor.getResult();
    }

    // RouterFunctions.Visitor

    @Override
    public void startNested(RequestPredicate predicate) {
        // TODO handle nesting?
    }

    @Override
    public void endNested(RequestPredicate predicate) {
        // ignored
    }

    @Override
    public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
        predicate.accept(this);
    }

    @Override
    public void resources(Function<ServerRequest, Mono<Resource>> lookupFunction) {
        // ignored
    }

    @Override
    public void unknown(RouterFunction<?> routerFunction) {
        // ignored
    }

    // RequestPredicates.Visitor

    @Override
    public void method(Set<HttpMethod> methods) {
        methods.stream()
                .map(Enum::name)
                .map(RequestMethod::valueOf)
                .forEach(result.requestMethods::add);
    }

    @Override
    public void path(String pattern) {
        result.paths.add(pattern);
    }

    @Override
    public void pathExtension(String extension) {
        // ignored
    }

    @Override
    public void header(String name, String value) {
        // client sends those headers and we match against it,
        // so it wants to consume what we produce
        if (HttpHeaders.ACCEPT.equals(name)) {
            // see also org.springframework.web.reactive.function.server.RequestPredicates.AcceptPredicate
            result.producesContentTypesFromHeader.add(value);
        } else if (HttpHeaders.CONTENT_TYPE.equals(name)) {
            // see also org.springframework.web.reactive.function.server.RequestPredicates.ContentTypePredicate
            result.consumesContentTypesFromHeader.add(value);
        } else {
            result.parameters.add(new RouterFunctionHandlerMethod.Parameter(name, ParameterIn.HEADER));
        }
    }

    @Override
    public void queryParam(String name, String value) {
        result.parameters.add(new RouterFunctionHandlerMethod.Parameter(name, ParameterIn.QUERY));
    }

    @Override
    public void startAnd() {
        // ignored
    }

    @Override
    public void and() {
        // ignored
    }

    @Override
    public void endAnd() {
        // ignored
    }

    @Override
    public void startOr() {
        // ignored
    }

    @Override
    public void or() {
        // ignored

    }

    @Override
    public void endOr() {
        // ignored
    }

    @Override
    public void startNegate() {
        // ignored
    }

    @Override
    public void endNegate() {
        // ignored
    }

    @Override
    public void unknown(RequestPredicate predicate) {
        // ignored
    }

    @Getter
    static class Result {
        private final Set<RequestMethod> requestMethods = new LinkedHashSet<>();
        private final Set<String> paths = new LinkedHashSet<>();
        private final List<HandlerMethod.Parameter> parameters = new ArrayList<>();
        private final List<String> consumesContentTypesFromHeader = new ArrayList<>();
        private final List<String> producesContentTypesFromHeader = new ArrayList<>();
    }
}
