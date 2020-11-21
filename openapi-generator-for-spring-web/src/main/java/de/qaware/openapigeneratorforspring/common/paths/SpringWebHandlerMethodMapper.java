package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @RequiredArgsConstructor
    public static class ResponseMapper implements HandlerMethod.ResponseMapper {

        private final AnnotationsSupplierFactory annotationsSupplierFactory;

        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof AbstractSpringWebHandlerMethod) {
                return ((AbstractSpringWebHandlerMethod) handlerMethod).getResponses(annotationsSupplierFactory);
            }
            return null; // indicates we can't map this handler method instance
        }
    }

    public static class ResponseCodeMapper implements HandlerMethod.ResponseCodeMapper {
        @Override
        @Nullable
        public String map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                ResponseStatus responseStatus = handlerMethod.getAnnotationsSupplier().findFirstAnnotation(ResponseStatus.class);
                if (responseStatus != null) {
                    return Integer.toString(responseStatus.code().value());
                }
                return Integer.toString(HttpStatus.OK.value());
            }
            return null; // indicates we can't map this handler method instance
        }
    }

    static Set<String> ifEmptyUseSingleAllValue(Set<String> contentTypes) {
        return contentTypes.isEmpty() ? singleton(org.springframework.http.MediaType.ALL_VALUE) : contentTypes;
    }
}
