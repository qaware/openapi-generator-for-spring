package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class HandlerMethodsProviderForWebFlux implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        springWebHandlerMethodBuilder.build(entry.getValue()),
                        entry.getKey().getPatternsCondition().getPatterns().stream()
                                .map(PathPattern::toString)
                                .collect(Collectors.toCollection(LinkedHashSet::new)),
                        entry.getKey().getMethodsCondition().getMethods()
                ))
                .collect(Collectors.toList());
    }
}
