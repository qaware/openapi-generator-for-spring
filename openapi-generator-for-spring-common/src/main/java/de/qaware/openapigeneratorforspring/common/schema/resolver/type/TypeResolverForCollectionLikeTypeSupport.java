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

package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;

@RequiredArgsConstructor
public class TypeResolverForCollectionLikeTypeSupport {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    public InitialType build(Object caller, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        val arraySchemaAnnotations = annotationsSupplier.findAnnotations(ArraySchema.class)
                .collect(Collectors.toCollection(LinkedList::new));
        return new CollectionLikeInitialType(
                javaType,
                extendAnnotationsSupplier(annotationsSupplier, arraySchemaAnnotations, ArraySchema::arraySchema),
                arraySchemaAnnotations,
                caller
        );
    }

    @Nullable
    public Schema buildFromType(Object caller, InitialType initialType) {
        if (CollectionLikeInitialType.matches(caller, initialType)) {
            val arraySchemaInitialType = (CollectionLikeInitialType) initialType;
            return buildArraySchema(arraySchemaInitialType.getArraySchemaAnnotations());
        }
        return null;
    }

    @Nullable
    public TypeResolver.RecursionKey resolve(
            Object caller, Schema schema, InitialType initialType,
            Supplier<JavaType> contentTypeSupplier, TypeResolver.SchemaBuilderFromType schemaBuilderFromType
    ) {
        if (CollectionLikeInitialType.matches(caller, initialType)) {
            JavaType contentType = contentTypeSupplier.get();
            val collectionLikeInitialType = (CollectionLikeInitialType) initialType;
            // do not use the current annotationsSupplier, but only use annotations directly present on contentType
            // and add possible schema annotations from the array schema annotation
            AnnotationsSupplier annotationsSupplierFromContentType = extendAnnotationsSupplier(
                    annotationsSupplierFactory.createFromAnnotatedElement(contentType.getRawClass()),
                    collectionLikeInitialType.getArraySchemaAnnotations(), ArraySchema::schema
            );
            schemaBuilderFromType.buildSchemaFromType(contentType, annotationsSupplierFromContentType, schema::setItems);
        }
        return null; // collections never create cyclic dependencies
    }

    private CollectionLikeSchema buildArraySchema(LinkedList<ArraySchema> arraySchemaAnnotations) {
        val arraySchema = new CollectionLikeSchema();
        arraySchemaAnnotations.descendingIterator()
                .forEachRemaining(arraySchemaAnnotation -> applyFromAnnotation(arraySchema, arraySchemaAnnotation));
        return arraySchema;
    }

    private void applyFromAnnotation(Schema schema, ArraySchema annotation) {
        if (annotation.uniqueItems()) {
            schema.setUniqueItems(true);
        }
        if (annotation.minItems() != Integer.MAX_VALUE) {
            schema.setMinItems(annotation.minItems());
        }
        if (annotation.maxItems() != Integer.MIN_VALUE) {
            schema.setMaxItems(annotation.maxItems());
        }
        mergeWithExistingMap(schema::getExtensions, schema::setExtensions, extensionAnnotationMapper.mapArray(annotation.extensions()));
    }

    private AnnotationsSupplier extendAnnotationsSupplier(
            AnnotationsSupplier annotationsSupplier,
            Collection<ArraySchema> arraySchemaAnnotations,
            Function<ArraySchema, io.swagger.v3.oas.annotations.media.Schema> mapper
    ) {
        if (arraySchemaAnnotations.isEmpty()) {
            return annotationsSupplier;
        }
        AnnotationsSupplier annotationsSupplierFromArraySchema = new AnnotationsSupplier() {
            @Override
            public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                if (annotationType.equals(io.swagger.v3.oas.annotations.media.Schema.class)) {
                    return arraySchemaAnnotations.stream()
                            .map(mapper)
                            .map(annotationType::cast);
                }
                return Stream.empty();
            }
        };
        return annotationsSupplierFromArraySchema.andThen(annotationsSupplier);
    }

    @EqualsAndHashCode(callSuper = true)
    private static class CollectionLikeSchema extends Schema {
        public CollectionLikeSchema() {
            setType("array");
        }
    }

    private static class CollectionLikeInitialType extends InitialType {

        @Getter
        private final LinkedList<ArraySchema> arraySchemaAnnotations;
        private final Object caller;

        protected CollectionLikeInitialType(
                JavaType type, AnnotationsSupplier annotationsSupplier,
                LinkedList<ArraySchema> arraySchemaAnnotations, Object caller
        ) {
            super(type, annotationsSupplier);
            this.arraySchemaAnnotations = arraySchemaAnnotations;
            this.caller = caller;
        }

        static boolean matches(Object caller, InitialType initialType) {
            return initialType instanceof CollectionLikeInitialType && ((CollectionLikeInitialType) initialType).caller.equals(caller);
        }
    }
}
