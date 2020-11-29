/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
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

package de.qaware.openapigeneratorforspring.common.annotation;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationFilter;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultAnnotationsSupplierFactory implements AnnotationsSupplierFactory {

    @Override
    public AnnotationsSupplier createFromMember(AnnotatedMember annotatedMember) {
        // using annotatedMember.getAnnotatedElement() and falling back
        // to the other method is not correct, as it doesn't find annotations specified on fields
        return new AnnotationSupplierFromMember(annotatedMember);
    }

    @Override
    public AnnotationsSupplier createFromAnnotatedElement(AnnotatedElement annotatedElement) {
        return new AnnotationsSupplierFromAnnotatedElement(annotatedElement);
    }

    private abstract static class RepeatableContainerAwareAnnotationsSupplier implements AnnotationsSupplier {
        @Override
        public final <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
            // always append possible repeatable annotations
            return Stream.concat(findAnnotationsDelegate(annotationType), findFromRepeatableContainer(annotationType));
        }

        private <A extends Annotation> Stream<A> findFromRepeatableContainer(Class<A> annotationType) {
            // important to always use findAnnotationsDelegate here,
            // otherwise we cause a Stackoverflow due to infinite recursion
            return new AnnotationsSupplierFromAnnotatedElement(annotationType).findAnnotationsDelegate(Repeatable.class)
                    .findFirst()
                    .map(Repeatable::value)
                    .map(this::findAnnotationsDelegate)
                    .orElseGet(Stream::empty)
                    // if, for example, annotationType is @ApiResponse.class,
                    // then the repeatableAnnotationContainer is of type @ApiResponses.class here,
                    // so the @Repeatable annotation tells us the type of repeatableAnnotationContainer
                    .flatMap(repeatableAnnotationContainer -> Stream.of(repeatableAnnotationContainer.getClass().getMethods())
                            // find "value" method getter(s) and invoke them on the repeatableAnnotationContainer
                            // to obtain the contained annotations
                            .filter(method -> MergedAnnotation.VALUE.equals(method.getName()))
                            .filter(method -> method.getParameterCount() == 0)
                            .flatMap(valueMethodGetter -> {
                                // there should only be one method getter but we don't mind invoking multiple of them,
                                // they're simply flat mapped anyway
                                try {
                                    Object result = valueMethodGetter.invoke(repeatableAnnotationContainer);
                                    return result instanceof Object[] ? Arrays.stream((Object[]) result) : Stream.empty();
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    // getter method should always be invokable
                                    throw new IllegalStateException("Cannot invoke value method getter " + valueMethodGetter, e);
                                }
                            })
                            .filter(annotationType::isInstance)
                            .map(annotationType::cast)
                    );
        }

        protected abstract <A extends Annotation> Stream<A> findAnnotationsDelegate(Class<A> annotationType);
    }

    @RequiredArgsConstructor
    private static class AnnotationSupplierFromMember extends RepeatableContainerAwareAnnotationsSupplier {
        private final AnnotatedMember annotatedMember;

        @Override
        public <A extends Annotation> Stream<A> findAnnotationsDelegate(Class<A> annotationType) {
            return Optional.ofNullable(annotatedMember.getAnnotation(annotationType))
                    .map(Stream::of)
                    .orElse(Stream.empty());
        }
    }

    private static class AnnotationsSupplierFromAnnotatedElement extends RepeatableContainerAwareAnnotationsSupplier {
        private final AnnotatedElement annotatedElement;
        private final MergedAnnotations mergedAnnotations;

        public AnnotationsSupplierFromAnnotatedElement(AnnotatedElement annotatedElement) {
            this.annotatedElement = annotatedElement;
            this.mergedAnnotations = MergedAnnotations.from(
                    annotatedElement,
                    MergedAnnotations.SearchStrategy.TYPE_HIERARCHY,
                    RepeatableContainers.standardRepeatables()
            );
        }

        @Override
        public <A extends Annotation> Stream<A> findAnnotationsDelegate(Class<A> annotationType) {
            if (AnnotationFilter.PLAIN.matches(annotationType)) {
                // PLAIN annotations are ignored by merged annotations API, see
                // https://github.com/spring-projects/spring-framework/issues/24932
                return Arrays.stream(annotatedElement.getAnnotationsByType(annotationType));
            } else {
                return mergedAnnotations.stream(annotationType).map(MergedAnnotation::synthesize);
            }
        }
    }
}
