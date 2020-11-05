package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;

import javax.annotation.Nullable;

public class RouterFunctionHandlerMethodMapper {

    public static class RequestBodyParameterMapper implements HandlerMethod.RequestBodyParameterMapper {
        @Nullable
        @Override
        public HandlerMethod.RequestBodyParameter map(HandlerMethod.Parameter parameter) {
            // TODO implement when dummy parameter type is seen on non-empty content type header?
            return null;
        }
    }

    public static class ReturnTypeMapper implements HandlerMethod.ReturnTypeMapper {
        @Nullable
        @Override
        public HandlerMethod.ReturnType map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof RouterFunctionHandlerMethod) {
                RouterFunctionHandlerMethod routerFunctionHandlerMethod = (RouterFunctionHandlerMethod) handlerMethod;
                io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = handlerMethod.getAnnotationsSupplier()
                        .findFirstAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                if (schemaAnnotation != null) {
                    return new RouterFunctionHandlerMethod.ReturnType(
                            routerFunctionHandlerMethod.getRouterFunctionAnalysisResult().getProducesContentTypesFromHeader()
                    );
                }
            }
            return null;
        }
    }
}
