package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SpringWebHandlerMethodBuilder {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public HandlerMethod build(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        Method method = springWebHandlerMethod.getMethod();
        return new SpringWebHandlerMethod(
                method.getName(),
                annotationsSupplierFactory.createFromMethodWithDeclaringClass(method),
                method.getReturnType(),
                method.getGenericReturnType(),
                Stream.of(method.getParameters())
                        .map(parameter -> new SpringWebHandlerMethodParameter(
                                parameter.getName(),
                                annotationsSupplierFactory.createFromAnnotatedElement(parameter),
                                annotationsSupplierFactory.createFromAnnotatedElement(parameter.getType()),
                                parameter.getParameterizedType()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
