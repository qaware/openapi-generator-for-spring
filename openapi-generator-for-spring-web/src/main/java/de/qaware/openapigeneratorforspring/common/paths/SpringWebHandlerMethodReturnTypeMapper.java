package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class SpringWebHandlerMethodReturnTypeMapper implements HandlerMethodReturnTypeMapper {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Nullable
    @Override
    public HandlerMethod.ReturnType map(HandlerMethod handlerMethod) {
        if (handlerMethod instanceof SpringWebHandlerMethod) {
            SpringWebHandlerMethod springWebHandlerMethod = (SpringWebHandlerMethod) handlerMethod;
            Class<?> returnType = springWebHandlerMethod.getReturnType();
            if (Void.TYPE.equals(returnType) || Void.class.equals(returnType)) {
                return null;
            }
            return new SpringWebHandlerMethodReturnType(
                    springWebHandlerMethod.getGenericReturnType(),
                    annotationsSupplierFactory.createFromAnnotatedElement(returnType)
            );
        }
        return null;
    }
}
