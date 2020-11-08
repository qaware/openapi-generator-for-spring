package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebHandlerMethodMapper {

    public static class ParameterTypeMapper implements HandlerMethod.ParameterTypeMapper {
        @Nullable
        @Override
        public Type map(HandlerMethod.Parameter parameter) {
            if (parameter instanceof SpringWebHandlerMethod.SpringWebParameter) {
                return ((SpringWebHandlerMethod.SpringWebParameter) parameter).getParameter().getParameterizedType();
            }
            return null;
        }
    }

    public static class RequestBodyParameterMapper implements HandlerMethod.RequestBodyParameterMapper {
        @Nullable
        @Override
        public HandlerMethod.RequestBodyParameter map(HandlerMethod.Parameter parameter) {
            if (parameter instanceof SpringWebHandlerMethod.SpringWebParameter) {
                SpringWebHandlerMethod.SpringWebParameter springWebParameter = (SpringWebHandlerMethod.SpringWebParameter) parameter;
                org.springframework.web.bind.annotation.RequestBody springWebRequestBodyAnnotation = springWebParameter.getAnnotationsSupplier().findFirstAnnotation(org.springframework.web.bind.annotation.RequestBody.class);
                if (springWebRequestBodyAnnotation != null) {
                    return new SpringWebHandlerMethod.SpringWebRequestBodyParameter(springWebParameter.getParentMethod().getAnnotationsSupplier(), springWebRequestBodyAnnotation);
                }
            }
            return null;
        }
    }

    @RequiredArgsConstructor
    public static class ReturnTypeMapper implements HandlerMethod.ReturnTypeMapper {

        private final AnnotationsSupplierFactory annotationsSupplierFactory;

        @Nullable
        @Override
        public HandlerMethod.ReturnType map(HandlerMethod handlerMethod) {
            if (handlerMethod instanceof SpringWebHandlerMethod) {
                SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
                Class<?> returnType = springWebHandlerMethod.getMethod().getReturnType();
                if (Void.TYPE.equals(returnType) || Void.class.equals(returnType)) {
                    return null;
                }
                return new SpringWebHandlerMethod.SpringWebReturnType(
                        // using method.getReturnType() does not work for generic return types
                        springWebHandlerMethod.getMethod().getGenericReturnType(),
                        annotationsSupplierFactory.createFromAnnotatedElement(returnType),
                        handlerMethod.getAnnotationsSupplier()
                );
            }
            return null;
        }
    }

    public static class ApiResponseCodeMapper implements HandlerMethod.ApiResponseCodeMapper {
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
