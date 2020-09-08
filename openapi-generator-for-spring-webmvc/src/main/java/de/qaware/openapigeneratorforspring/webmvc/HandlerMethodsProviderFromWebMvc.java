package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HandlerMethodsProviderFromWebMvc implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        entry.getValue(),
                        entry.getKey().getPatternsCondition().getPatterns(),
                        entry.getKey().getMethodsCondition().getMethods()
                ))
                .collect(Collectors.toList());
    }
}
