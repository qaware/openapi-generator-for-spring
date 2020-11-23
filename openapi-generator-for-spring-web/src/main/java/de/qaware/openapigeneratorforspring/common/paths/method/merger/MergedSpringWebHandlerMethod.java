package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;

@Getter
public class MergedSpringWebHandlerMethod extends AbstractSpringWebHandlerMethod {
    private final String identifier;
    private final List<SpringWebHandlerMethod> handlerMethods;

    public MergedSpringWebHandlerMethod(List<Parameter> parameters, String identifier, List<SpringWebHandlerMethod> handlerMethods) {
        super(parameters);
        this.identifier = identifier;
        this.handlerMethods = handlerMethods;
    }

    @Override
    public <A extends Annotation> ContextAwareAnnotations<A> findAnnotationsWithContext(Class<A> annotationType) {
        return () -> handlerMethods.stream().flatMap(handlerMethod -> handlerMethod.findAnnotations(annotationType));
    }
}
