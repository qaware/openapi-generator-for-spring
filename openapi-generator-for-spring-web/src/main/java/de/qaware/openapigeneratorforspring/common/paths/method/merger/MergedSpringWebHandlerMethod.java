package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.method.AbstractSpringWebHandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

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
        return new ContextAwareAnnotations<A>() {
            @Override
            public Stream<A> asStream() {
                return handlerMethods.stream().flatMap(handlerMethod -> handlerMethod.findAnnotations(annotationType));
            }

            @Override
            public <R> Stream<R> map(BiFunction<? super A, Context, ? extends R> mapper) {
                return handlerMethods.stream()
                        .flatMap(handlerMethod -> handlerMethod.findAnnotations(annotationType)
                                .map(annotation -> mapper.apply(annotation, handlerMethod))
                        );
            }

            @Override
            public void forEach(BiConsumer<? super A, Context> action) {
                handlerMethods.forEach(handlerMethod ->
                        handlerMethod.findAnnotations(annotationType)
                                .forEach(annotation -> action.accept(annotation, handlerMethod))
                );
            }
        };
    }
}
