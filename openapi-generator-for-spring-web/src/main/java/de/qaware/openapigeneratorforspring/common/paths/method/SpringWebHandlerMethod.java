package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@ToString(onlyExplicitlyIncluded = true)
public class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod implements HandlerMethod.Context {

    @Getter
    @ToString.Include
    private final Method method;
    private final AnnotationsSupplier annotationsSupplier;

    public SpringWebHandlerMethod(AnnotationsSupplier annotationsSupplier, List<Parameter> parameters, Method method) {
        super(parameters);
        this.method = method;
        this.annotationsSupplier = annotationsSupplier;
    }

    @Override
    public String getIdentifier() {
        return method.getName();
    }

    @Override
    public <A extends Annotation> ContextAwareAnnotations<A> findAnnotationsWithContext(Class<A> annotationType) {
        HandlerMethod.Context context = this;
        return new HandlerMethod.ContextAwareAnnotations<A>() {
            @Override
            public Stream<A> asStream() {
                return annotationsSupplier.findAnnotations(annotationType);
            }

            @Override
            public <R> Stream<R> map(BiFunction<? super A, Context, ? extends R> mapper) {
                return asStream().map(annotation -> mapper.apply(annotation, context));
            }

            @Override
            public void forEach(BiConsumer<? super A, Context> action) {
                asStream().forEach(annotation -> action.accept(annotation, context));
            }
        };
    }
}
