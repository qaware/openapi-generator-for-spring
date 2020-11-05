package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodsMapper;
import de.qaware.openapigeneratorforspring.webflux.function.RouterFunctionHandlerMethodWithInfoBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class HandlerMethodsProviderForWebFlux implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder;
    private final SpringWebRequestMethodsMapper springWebRequestMethodsMapper;

    private final Map<String, RouterFunction<?>> routerFunctions;
    private final RouterFunctionHandlerMethodWithInfoBuilder routerFunctionHandlerMethodWithInfoBuilder;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return Stream.concat(
                getHandlerMethodsFromRequestMappings(),
                getHandlerMethodsFromRouterFunctions()
        )
                .collect(Collectors.toList());
    }

    private Stream<HandlerMethodWithInfo> getHandlerMethodsFromRequestMappings() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        springWebHandlerMethodBuilder.build(entry.getValue()),
                        entry.getKey().getPatternsCondition().getPatterns().stream()
                                .map(PathPattern::toString)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                        springWebRequestMethodsMapper.map(entry.getKey().getMethodsCondition().getMethods())
                ));
    }

    private Stream<HandlerMethodWithInfo> getHandlerMethodsFromRouterFunctions() {
        return routerFunctions.entrySet().stream().map(entry -> routerFunctionHandlerMethodWithInfoBuilder.build(entry.getKey(), entry.getValue()));
    }
}
