package de.qaware.openapigeneratorforspring.webflux.function;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouterFunctionHandlerMethodMapper {

    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {
        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            // TODO implement when dummy parameter type is seen on non-empty content type header?
            return Collections.emptyList();
        }
    }

    public static class ResponseMapper implements HandlerMethod.ResponseMapper {
        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof RouterFunctionHandlerMethod) {
                RouterFunctionHandlerMethod routerFunctionHandlerMethod = (RouterFunctionHandlerMethod) handlerMethod;
                RouterFunctionHandlerMethod.Response returnType = new RouterFunctionHandlerMethod.Response(
                        routerFunctionHandlerMethod.getRouterFunctionAnalysisResult().getProducesContentTypesFromHeader(),
                        // Schema building could still use @Schema annotation from bean factory method,
                        // so supply a "dummy" type here for schema building
                        new RouterFunctionHandlerMethod.RouterFunctionType(AnnotationsSupplier.EMPTY, Void.class)
                );
                return Collections.singletonList(returnType);
            }
            return null;
        }
    }
}
