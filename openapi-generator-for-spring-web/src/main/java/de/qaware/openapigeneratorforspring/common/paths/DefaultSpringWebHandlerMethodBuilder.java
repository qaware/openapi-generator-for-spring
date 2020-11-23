package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultSpringWebHandlerMethodBuilder implements SpringWebHandlerMethodBuilder {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Override
    public HandlerMethod build(org.springframework.web.method.HandlerMethod springWebHandlerMethod) {
        Method method = springWebHandlerMethod.getMethod();
        return new SpringWebHandlerMethod(
                annotationsSupplierFactory.createFromMethodWithDeclaringClass(method),
                buildParameters(method.getParameters()),
                method
        );
    }

    private List<HandlerMethod.Parameter> buildParameters(java.lang.reflect.Parameter[] parameters) {
        return Stream.of(parameters)
                .map(this::buildParameter)
                .collect(Collectors.toList());
    }

    private AbstractSpringWebHandlerMethod.SpringWebParameter buildParameter(java.lang.reflect.Parameter parameter) {
        return new AbstractSpringWebHandlerMethod.SpringWebParameter(
                // avoid building with some "arg1" auto-generated parameters
                parameter.isNamePresent() ? parameter.getName() : null,
                AbstractSpringWebHandlerMethod.SpringWebType.of(parameter.getParameterizedType(), annotationsSupplierFactory.createFromAnnotatedElement(parameter.getType())),
                annotationsSupplierFactory.createFromAnnotatedElement(parameter)
        );
    }
}
