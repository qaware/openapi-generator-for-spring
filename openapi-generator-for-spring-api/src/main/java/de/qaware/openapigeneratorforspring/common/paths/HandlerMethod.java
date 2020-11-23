package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.common.annotation.HasAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface HandlerMethod {
    String getIdentifier();

    List<Parameter> getParameters();

    default <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
        return findAnnotationsWithContext(annotationType).asStream();
    }

    <A extends Annotation> ContextAwareAnnotations<A> findAnnotationsWithContext(Class<A> annotationType);

    /**
     * Empty marker interface for {@link ContextAwareAnnotations} and {@link ContextModifierMapper}.
     */
    interface Context {

    }

    @FunctionalInterface
    interface ContextModifier<T> {
        T modify(T context);
    }

    @FunctionalInterface
    interface ContextAwareAnnotations<T> {
        Stream<T> asStream();

        default <R> Stream<R> map(BiFunction<? super T, Context, ? extends R> mapper) {
            return asStream().map(item -> mapper.apply(item, null));
        }

        default void forEach(BiConsumer<? super T, Context> action) {
            asStream().forEach(item -> action.accept(item, null));
        }
    }

    interface Type extends HasAnnotationsSupplier {
        java.lang.reflect.Type getType();
    }

    interface HasType {
        Optional<Type> getType();
    }

    interface Parameter extends HasAnnotationsSupplier, HasType {
        Optional<String> getName();

        // TODO check if this context is actually of use?
        @Nullable
        default Context getContext() {
            return null;
        }

        // TODO use this with extensions to indicate where a parameter is useful?
        default void customize(de.qaware.openapigeneratorforspring.model.parameter.Parameter parameter) {
            // do nothing, customization callback is optional
        }
    }


    interface RequestBody extends HasAnnotationsSupplier, HasType {

        Set<String> getConsumesContentTypes();

        @Nullable
        default Context getContext() {
            return null;
        }

        default void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            // do nothing, customization callback is optional
        }

    }

    interface Response extends HasType {
        String getResponseCode();

        Set<String> getProducesContentTypes();

        default void customize(ApiResponse apiResponse) {
            // do nothing, customization callback is optional
        }
    }

    @FunctionalInterface
    @Order(0)
    interface ContextModifierMapper<T> {
        @Nullable
        ContextModifier<T> map(@Nullable Context context);
    }

    @FunctionalInterface
    @Order(0)
    interface RequestBodyMapper {
        @Nullable
        List<RequestBody> map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface ResponseMapper {
        @Nullable
        List<Response> map(HandlerMethod handlerMethod);
    }

    @FunctionalInterface
    @Order(0)
    interface Merger {
        @Nullable
        HandlerMethod merge(List<HandlerMethod> handlerMethods);
    }
}
