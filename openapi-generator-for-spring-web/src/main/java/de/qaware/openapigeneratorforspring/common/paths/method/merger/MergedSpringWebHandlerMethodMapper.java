package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.mapper.MapperContext;
import de.qaware.openapigeneratorforspring.common.mapper.MediaTypesProvider;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.requestbody.RequestBody;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MergedSpringWebHandlerMethodMapper {

    public static class ContextModifierMapper implements HandlerMethod.ContextModifierMapper<MapperContext> {
        @Nullable
        @Override
        public HandlerMethod.ContextModifier<MapperContext> map(@Nullable HandlerMethod.Context context) {
            if (context instanceof MergedSpringWebHandlerMethodContext) {
                MergedSpringWebHandlerMethodContext mergedHandlerMethodContext = (MergedSpringWebHandlerMethodContext) context;
                MediaTypesProvider mediaTypesProvider = owningType -> {
                    if (RequestBody.class.equals(owningType)) {
                        return mergedHandlerMethodContext.getConsumesContentTypes();
                    }
                    throw new IllegalStateException("Cannot provide media types for " + owningType.getSimpleName() + " in merged method");
                };
                return mapperContext -> mapperContext.withMediaTypesProvider(mediaTypesProvider);
            }
            return null; // indicates we can't map this handler method context
        }
    }

    @RequiredArgsConstructor
    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {

        private final SpringWebHandlerMethodRequestBodyMerger requestBodyMerger;

        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof MergedSpringWebHandlerMethod) {
                MergedSpringWebHandlerMethod mergedSpringWebHandlerMethod = (MergedSpringWebHandlerMethod) handlerMethod;
                return requestBodyMerger.mergeRequestBodies(mergedSpringWebHandlerMethod.getHandlerMethods());
            }
            return null; // indicates we can't map this handler method instance
        }
    }

    @RequiredArgsConstructor
    public static class ResponseMapper implements HandlerMethod.ResponseMapper {

        private final SpringWebHandlerMethodResponseMerger responseMerger;

        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof MergedSpringWebHandlerMethod) {
                MergedSpringWebHandlerMethod mergedSpringWebHandlerMethod = (MergedSpringWebHandlerMethod) handlerMethod;
                return responseMerger.mergeResponses(mergedSpringWebHandlerMethod.getHandlerMethods());
            }
            return null; // indicates we can't map this handler method instance
        }
    }
}
