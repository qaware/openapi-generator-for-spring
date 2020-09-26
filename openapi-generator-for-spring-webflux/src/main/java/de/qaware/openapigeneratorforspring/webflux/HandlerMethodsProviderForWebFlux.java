package de.qaware.openapigeneratorforspring.webflux;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HandlerMethodsProviderForWebFlux implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        entry.getValue(),
                        entry.getKey().getPatternsCondition().getPatterns().stream()
                                .map(PathPattern::toString)
                                .collect(Collectors.toSet()),
                        entry.getKey().getMethodsCondition().getMethods()
                ))
                .collect(Collectors.toList());
    }
}
