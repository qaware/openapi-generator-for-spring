package de.qaware.openapigeneratorforspring.common.paths;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {
        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof AbstractSpringWebHandlerMethod) {
                return ((AbstractSpringWebHandlerMethod) handlerMethod).getRequestBodies();
            }
            return null; // indicates we can't map this handler method instance
        }
    }

    public static class ResponseMapper implements HandlerMethod.ResponseMapper {

        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof AbstractSpringWebHandlerMethod) {
                return ((AbstractSpringWebHandlerMethod) handlerMethod).getResponses();
            }
            return null; // indicates we can't map this handler method instance
        }
    }

    static Set<String> ifEmptyUseSingleAllValue(Set<String> contentTypes) {
        return contentTypes.isEmpty() ? singleton(org.springframework.http.MediaType.ALL_VALUE) : contentTypes;
    }
}
