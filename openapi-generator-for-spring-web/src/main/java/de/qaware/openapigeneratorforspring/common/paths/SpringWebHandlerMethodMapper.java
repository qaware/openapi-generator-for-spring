package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethod.SpringWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    public static class RequestBodyMapper implements HandlerMethod.RequestBodyMapper {
        @Nullable
        @Override
        public List<HandlerMethod.RequestBody> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof AbstractSpringWebHandlerMethod) {
                return ((AbstractSpringWebHandlerMethod) handlerMethod).getRequestBodies();
            }
            return null; // indicates we can't map this method
        }
    }

    @RequiredArgsConstructor
    public static class ResponseMapper implements HandlerMethod.ResponseMapper {

        private final AnnotationsSupplierFactory annotationsSupplierFactory;

        @Nullable
        @Override
        public List<HandlerMethod.Response> map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
                Method method = springWebHandlerMethod.getMethod();
                Class<?> returnType = method.getReturnType();
                List<String> producesContentTypes = findProducesContentTypes(handlerMethod.getAnnotationsSupplier());
                // using method.getReturnType() does not work for generic return types
                AbstractSpringWebHandlerMethod.SpringWebType springWebType = AbstractSpringWebHandlerMethod.SpringWebType.of(method.getGenericReturnType(), annotationsSupplierFactory.createFromAnnotatedElement(returnType));
                SpringWebResponse springWebReturnType = new SpringWebResponse(producesContentTypes, springWebType);
                return Collections.singletonList(springWebReturnType);
            }
            return null;
        }

        private List<String> findProducesContentTypes(AnnotationsSupplier handlerMethodAnnotationsSupplier) {
            // TODO check if that logic here correctly mimics the way Spring is treating the "produces" property
            return handlerMethodAnnotationsSupplier.findAnnotations(RequestMapping.class)
                    .filter(requestMappingAnnotation -> !StringUtils.isAllBlank(requestMappingAnnotation.produces()))
                    .findFirst()
                    .map(requestMappingAnnotation -> Arrays.asList(requestMappingAnnotation.produces()))
                    // fallback to "all value" if nothing has been specified
                    .orElse(singletonList(org.springframework.http.MediaType.ALL_VALUE));
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
            return null;
        }
    }
}
