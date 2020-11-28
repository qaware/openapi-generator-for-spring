package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class MergedSpringWebHandlerMethodContext implements HandlerMethod.Context {
    private final Set<String> consumesContentTypes;
}
