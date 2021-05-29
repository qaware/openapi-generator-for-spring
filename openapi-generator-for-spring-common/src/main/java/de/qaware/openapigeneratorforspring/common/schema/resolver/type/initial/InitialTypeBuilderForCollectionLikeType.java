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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InitialTypeBuilderForCollectionLikeType implements InitialTypeBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        if (javaType.isCollectionLikeType()) {
            LinkedList<ArraySchema> arraySchemaAnnotations = annotationsSupplier.findAnnotations(ArraySchema.class)
                    .collect(Collectors.toCollection(LinkedList::new));
            return new ArraySchemaInitialType(javaType,
                    extendAnnotationsSupplier(annotationsSupplier, arraySchemaAnnotations),
                    arraySchemaAnnotations
            );
        }
        return null;
    }

    private AnnotationsSupplier extendAnnotationsSupplier(AnnotationsSupplier annotationsSupplier, Collection<ArraySchema> arraySchemaAnnotations) {
        if (arraySchemaAnnotations.isEmpty()) {
            return annotationsSupplier;
        }
        AnnotationsSupplier annotationsSupplierFromArraySchema = new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                if (annotationType.equals(Schema.class)) {
                    return arraySchemaAnnotations.stream()
                            .map(ArraySchema::arraySchema)
                            .map(annotationType::cast);
                }
                return Stream.empty();
            }
        };
        return annotationsSupplierFromArraySchema.andThen(annotationsSupplier);
    }

    static class ArraySchemaInitialType extends InitialType {

        @Getter
        private final LinkedList<ArraySchema> arraySchemaAnnotations;

        protected ArraySchemaInitialType(JavaType type, AnnotationsSupplier annotationsSupplier, LinkedList<ArraySchema> arraySchemaAnnotations) {
            super(type, annotationsSupplier);
            this.arraySchemaAnnotations = arraySchemaAnnotations;
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
