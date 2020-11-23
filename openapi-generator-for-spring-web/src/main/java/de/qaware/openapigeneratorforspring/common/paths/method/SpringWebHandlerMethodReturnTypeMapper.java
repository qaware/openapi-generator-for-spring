package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SpringWebHandlerMethodReturnTypeMapper {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public HandlerMethod.Type getReturnType(SpringWebHandlerMethod springWebHandlerMethod) {
        Method method = springWebHandlerMethod.getMethod();
        // even for Void method return type, there might still be @Schema annotation which could be useful
        // using method.getReturnType() does not work for generic return types
        return AbstractSpringWebHandlerMethod.SpringWebType.of(method.getGenericReturnType(), annotationsSupplierFactory.createFromAnnotatedElement(method.getReturnType()));
    }
}
