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
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilder;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;

@RequiredArgsConstructor
public class TypeResolverForCollectionLikeType implements InitialTypeBuilder, InitialSchemaBuilder, TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Nullable
    @Override
    public InitialType build(JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        if (javaType.isCollectionLikeType()) {
            val arraySchemaAnnotations = annotationsSupplier.findAnnotations(ArraySchema.class)
                    .collect(Collectors.toCollection(LinkedList::new));
            return new CollectionLikeInitialType(javaType,
                    extendAnnotationsSupplier(annotationsSupplier, arraySchemaAnnotations, ArraySchema::arraySchema),
                    arraySchemaAnnotations
            );
        }
        return null;
    }

    @Nullable
    @Override
    public Schema buildFromType(InitialType initialType) {
        if (initialType instanceof CollectionLikeInitialType) {
            val arraySchemaInitialType = (CollectionLikeInitialType) initialType;
            return buildArraySchema(arraySchemaInitialType.getArraySchemaAnnotations(), Set.class.isAssignableFrom(initialType.getType().getRawClass()));
        }
        return null;
    }

    @Nullable
    @Override
    public RecursionKey resolve(SchemaResolver.Mode mode, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        if (initialType instanceof CollectionLikeInitialType) {
            val collectionLikeInitialType = (CollectionLikeInitialType) initialType;
            JavaType contentType = initialType.getType().getContentType();
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

    private CollectionLikeSchema buildArraySchema(LinkedList<ArraySchema> arraySchemaAnnotations, boolean isSetType) {
        val arraySchema = new CollectionLikeSchema(isSetType ? true : null);
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
        public CollectionLikeSchema(@Nullable Boolean uniqueItems) {
            setType("array");
            setUniqueItems(uniqueItems);
        }
    }

    private static class CollectionLikeInitialType extends InitialType {

        @Getter
        private final LinkedList<ArraySchema> arraySchemaAnnotations;

        protected CollectionLikeInitialType(JavaType type, AnnotationsSupplier annotationsSupplier,
                                            LinkedList<ArraySchema> arraySchemaAnnotations) {
            super(type, annotationsSupplier);
            this.arraySchemaAnnotations = arraySchemaAnnotations;
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
