/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Web
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

package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@ToString(onlyExplicitlyIncluded = true)
public class SpringWebHandlerMethod extends AbstractSpringWebHandlerMethod implements HandlerMethod.Context {

    @Getter
    @ToString.Include
    private final org.springframework.web.method.HandlerMethod method;
    private final AnnotationsSupplier annotationsSupplier;

    public SpringWebHandlerMethod(AnnotationsSupplier annotationsSupplier, List<SpringWebParameter> parameters, org.springframework.web.method.HandlerMethod method) {
        super(parameters);
        this.method = method;
        this.annotationsSupplier = annotationsSupplier;
    }

    @Override
    public String getIdentifier() {
        return method.getMethod().getName();
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
