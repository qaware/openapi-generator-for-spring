package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import javax.annotation.Nullable;

public class RouterFunctionParameterMethodConverter implements ParameterMethodConverter {

    public static int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public de.qaware.openapigeneratorforspring.model.parameter.Parameter convert(HandlerMethod.Parameter handlerMethodParameter) {
        if (handlerMethodParameter instanceof RouterFunctionHandlerMethod.Parameter) {
            RouterFunctionHandlerMethod.Parameter routerFunctionParameter = (RouterFunctionHandlerMethod.Parameter) handlerMethodParameter;
            de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter = new de.qaware.openapigeneratorforspring.model.parameter.Parameter();
            parameter.setName(routerFunctionParameter.getName());
            parameter.setIn(routerFunctionParameter.getParameterIn().toString());
            return parameter;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
