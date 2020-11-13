package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouterFunctionHandlerMethodMapper {

    public static class RequestBodyParameterMapper implements HandlerMethod.RequestBodyParameterMapper {
        @Nullable
        @Override
        public List<HandlerMethod.RequestBodyParameter> map(HandlerMethod handlerMethod) {
            // TODO implement when dummy parameter type is seen on non-empty content type header?
            return null;
        }
    }

    public static class ReturnTypeMapper implements HandlerMethod.ReturnTypeMapper {
        @Nullable
        @Override
        public List<HandlerMethod.ReturnType> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof RouterFunctionHandlerMethod) {
                RouterFunctionHandlerMethod routerFunctionHandlerMethod = (RouterFunctionHandlerMethod) handlerMethod;
                io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = handlerMethod.getAnnotationsSupplier()
                        .findFirstAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
                if (schemaAnnotation != null) {
                    RouterFunctionHandlerMethod.ReturnType returnType = new RouterFunctionHandlerMethod.ReturnType(
                            routerFunctionHandlerMethod.getRouterFunctionAnalysisResult().getProducesContentTypesFromHeader()
                    );
                    return Collections.singletonList(returnType);
                }
            }
            return null;
        }
    }
}
