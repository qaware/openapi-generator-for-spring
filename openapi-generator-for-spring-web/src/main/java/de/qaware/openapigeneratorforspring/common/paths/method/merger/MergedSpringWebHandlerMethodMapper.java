package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MergedSpringWebHandlerMethodMapper {

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
