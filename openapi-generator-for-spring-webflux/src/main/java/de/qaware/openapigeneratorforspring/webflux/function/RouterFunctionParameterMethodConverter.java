package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;

import javax.annotation.Nullable;

public class RouterFunctionParameterMethodConverter implements ParameterMethodConverter {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        if (handlerMethodParameter instanceof RouterFunctionHandlerMethod.Parameter) {
            RouterFunctionHandlerMethod.Parameter parameter = (RouterFunctionHandlerMethod.Parameter) handlerMethodParameter;
            return Parameter.builder()
                    .name(parameter.getName()
                            .orElseThrow(() -> new IllegalStateException("Router function parameter should always have a name")))
                    .in(parameter.getParameterIn().toString())
                    .build();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
