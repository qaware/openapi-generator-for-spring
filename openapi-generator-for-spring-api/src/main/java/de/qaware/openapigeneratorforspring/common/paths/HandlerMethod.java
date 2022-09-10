/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

/**
 * Abstract representation of a handler method for incoming
 * requests. Implementations know about Spring Web, Router
 * Functions or merged handlers, but this abstraction does not.
 *
 * <p> It only provides the absolutely necessary information for
 * constructing the OpenApi model, in particular their contained {@link
 * de.qaware.openapigeneratorforspring.model.operation.Operation operations} and
 * {@link de.qaware.openapigeneratorforspring.model.path.PathItem path items}.
 */
public interface HandlerMethod {
    /**
     * Identifier of this handler.
     *
     * <p> Uniqueness is guaranteed by {@link
     * de.qaware.openapigeneratorforspring.common.operation.id.OperationIdConflictResolver
     * OperationIdConflictResolver}.
     *
     * @return string, must not be unique
     */
    String getIdentifier();

    /**
     * List of {@link Parameter parameters}. May include parameters
     * which may not map to actual OpenApi spec parameters.
     *
     * @return parameters list of {@link Parameter parameters}.
     */
    List<Parameter> getParameters();

    /**
     * Helper method to find annotations relevant for this handler method.
     *
     * <p> See also {@link #findAnnotationsWithContext} which is why the {@link
     * HasAnnotationsSupplier} trait is not used for {@link HandlerMethod}.
     *
     * @param annotationType type of annotation
     * @param <A>            type of annotation
     * @return stream of annotations
     */
    default <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
        return findAnnotationsWithContext(annotationType).asStream();
    }

    /**
     * Provide annotations with optional {@link Context context}.
     *
     * @param annotationType type of annotation
     * @param <A>            type of annotation
     * @return stream of annotations with context awareness
     */
    <A extends Annotation> ContextAwareAnnotations<A> findAnnotationsWithContext(Class<A> annotationType);

    /**
     * Parameter of the handler method.
     * <p>
     * Provides annotations, a type, a context and can be customized.
     */
    interface Parameter extends HasAnnotationsSupplier, HasType, HasContext,
            HasCustomize<de.qaware.openapigeneratorforspring.model.parameter.Parameter> {
        /**
         * Optional name of the parameter. If the parameter is not present, it
         * must be filled via some other means (for example, via a customizer).
         *
         * @return optional parameter name
         */
        Optional<String> getName();
    }


    interface RequestBody extends HasAnnotationsSupplier, HasType, HasContext,
            HasCustomize<de.qaware.openapigeneratorforspring.model.requestbody.RequestBody> {
        Set<String> getConsumesContentTypes();
    }

    interface Response extends HasType, HasCustomize<ApiResponse> {
        String getResponseCode();

        Set<String> getProducesContentTypes();
    }

    /**
     * Mapper for {@link RequestBody request
     * bodies} from a given handler method.
     *
     * <p>Gives handler method implementations precise control which
     * request bodies are discovered for the OpenApi model. Can
     * also be replaced by customized mapper using {@link Order}.
     */
    @FunctionalInterface
    @Order(0)
    interface RequestBodyMapper {
        /**
         * Map from handler method to list of request bodies.
         * Returns {@code null} if handler method implementation is unknown.
         *
         * @param handlerMethod handler method
         * @return list of request bodies, or null if handler method is unknown
         */
        @Nullable
        List<RequestBody> map(HandlerMethod handlerMethod);
    }

    /**
     * Mapper for {@link Response responses} from a given handler method.
     *
     * <p>Gives handler method implementations precise control
     * which responses are discovered for the OpenApi model. Can
     * also be replaced by customized mapper using {@link Order}.
     */
    @FunctionalInterface
    @Order(0)
    interface ResponseMapper {
        /**
         * Map from handler method to list of responses. Returns
         * {@code null} if handler method implementation is unknown.
         *
         * @param handlerMethod handler method
         * @return list of responses, or null if handler method is unknown
         */
        @Nullable
        List<Response> map(HandlerMethod handlerMethod);
    }

    /**
     * If handler methods are encountered applying the same request
     * method (GET, POST, ...) and the same path, implementations
     * of this merger are invoked in given {@link Order order}.
     *
     * <p>Open Api model building will fail if no merger is found
     * or handler methods of incompatible types clash. May be
     * customized by providing own implementations of this interface.
     */
    @FunctionalInterface
    @Order(0)
    interface Merger {
        @Nullable
        HandlerMethod merge(List<HandlerMethod> handlerMethods);
    }

    /**
     * Empty marker interface for {@link ContextAwareAnnotations} and {@link ContextModifierMapper}.
     */
    interface Context {

    }

    /**
     * Interface to provide context during annotations
     * streaming. See {@link #findAnnotationsWithContext}.
     *
     * @param <A> type of annotations
     * @implNote Implementations should override both {@link
     * #map} and {@link #forEach} for a consistent behavior.
     */
    @FunctionalInterface
    interface ContextAwareAnnotations<A extends Annotation> {
        /**
         * Fallback method to conventional stream.
         *
         * @return stream of annotations
         */
        Stream<A> asStream();

        /**
         * Map the annotations using {@link Context}.
         * Note that it also falls back to a conventional stream of mapped items.
         *
         * @param mapper mapper
         * @param <R>    return type of mapper
         * @return stream of mapped items
         */
        default <R> Stream<R> map(BiFunction<? super A, Context, ? extends R> mapper) {
            return asStream().map(item -> mapper.apply(item, null));
        }

        /**
         * Consume the annotations using {@link Context}
         *
         * @param action action to consume the annotation with context
         */
        default void forEach(BiConsumer<? super A, Context> action) {
            asStream().forEach(item -> action.accept(item, null));
        }
    }

    /**
     * Mapper to find a {@link ContextModifier} from
     * handler method {@link Context}.
     *
     * @param <C> type of context to be modified
     */
    @FunctionalInterface
    @Order(0)
    interface ContextModifierMapper<C> {
        /**
         * Map from {@link Context} to {@link ContextModifier}.
         *
         * @param context context, maybe null
         * @return modifier, maybe null if given context is not known
         */
        @Nullable
        ContextModifier<C> map(@Nullable Context context);
    }

    /**
     * Modifier for an arbitrary context of type {@link C}.
     *
     * <p>Implementations may decide if they actually return a modified version
     * of the context, otherwise they can just return the given context.
     *
     * @param <C> type of context
     */
    @FunctionalInterface
    interface ContextModifier<C> {
        /**
         * Use the given context to return a modified one.
         *
         * @param context given context
         * @return modified context
         */
        C modify(C context);
    }


    /**
     * Interface representing a {@link
     * java.lang.reflect.Type Java Type} including {@link
     * de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier
     * annotations}.
     *
     * <p>Each type here may have its own set of annotations, which are in
     * general different from the element carrying this type.
     *
     * <p>Note that this interface provides exactly
     * the information needed by the  {@link
     * de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver#resolveFromType schema resolver}
     */
    interface Type extends HasAnnotationsSupplier {
        java.lang.reflect.Type getType();
    }

    /**
     * Trait having an optional {@link Type}. Making this optional
     * gives implementations the freedom to not specify a type.
     */
    interface HasType {
        Optional<Type> getType();
    }

    /**
     * Trait for having more specific {@link Context}
     * and the handler method provides with annotations.
     */
    interface HasContext {
        /**
         * Get the context, returns {@code null} by default.
         *
         * @return context or null
         */
        @Nullable
        default Context getContext() {
            // by default, do not offer any context
            return null;
        }
    }

    /**
     * Trait for being customized by the different handler method implementations.
     *
     * @param <T> type of the object being customized
     */
    interface HasCustomize<T> {
        /**
         * Customize the given item via reference.
         *
         * @param item item to be modified
         */
        default void customize(T item) {
            // do nothing, customization callback is optional
        }
    }
}
