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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.mergeWithExistingMap;

@RequiredArgsConstructor
public class TypeResolverForCollectionLikeTypeSupport {

    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final OpenApiObjectMapperSupplier objectMapperSupplier;

    @Nullable
    public InitialType build(Object caller, Predicate<JavaType> condition, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        JavaType unpackedJavaType = unpackJacksonResolvedRecursiveType(javaType);

        if (condition.test(unpackedJavaType)) {
            val arraySchemaAnnotations = annotationsSupplier.findAnnotations(ArraySchema.class)
                    .collect(Collectors.toCollection(LinkedList::new));
            return new CollectionLikeInitialType(
                    unpackedJavaType,
                    extendAnnotationsSupplier(annotationsSupplier, arraySchemaAnnotations, ArraySchema::arraySchema),
                    arraySchemaAnnotations,
                    unpackedJavaType != javaType,
                    caller
            );
        }
        return null;
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
            return collectionLikeInitialType.isRecursiveJacksonType() ? new UniqueSchemaKey(initialType.getType(), getSchemaSnapshot(schema)) : null;
        }
        return null;
    }

    private String getSchemaSnapshot(Schema schema) {
        try {
            return objectMapperSupplier.get(SCHEMA_BUILDING).writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialized schema for snapshot", e);
        }
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

    private static JavaType unpackJacksonResolvedRecursiveType(JavaType javaType) {
        if (javaType instanceof ResolvedRecursiveType) {
            JavaType selfReferencedType = ((ResolvedRecursiveType) javaType).getSelfReferencedType();
            if (selfReferencedType != null) {
                return selfReferencedType;
            }
        }
        return javaType;
    }

    private static AnnotationsSupplier extendAnnotationsSupplier(
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
        @Getter
        private final boolean recursiveJacksonType;
        private final Object caller;

        protected CollectionLikeInitialType(
                JavaType type, AnnotationsSupplier annotationsSupplier,
                LinkedList<ArraySchema> arraySchemaAnnotations, boolean recursiveJacksonType, Object caller
        ) {
            super(type, annotationsSupplier);
            this.arraySchemaAnnotations = arraySchemaAnnotations;
            this.recursiveJacksonType = recursiveJacksonType;
            this.caller = caller;
        }

        static boolean matches(Object caller, InitialType initialType) {
            return initialType instanceof CollectionLikeInitialType && ((CollectionLikeInitialType) initialType).caller.equals(caller);
        }
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode // important!
    private static class UniqueSchemaKey implements TypeResolver.RecursionKey {
        private final JavaType javaType;
        private final String schemaSnapshot;
    }
}
