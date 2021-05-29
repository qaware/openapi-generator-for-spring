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
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;

public class TypeResolverForCollectionLikeType extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public TypeResolverForCollectionLikeType(InitialSchemaBuilderForCollectionLikeType typeResolverSupport, AnnotationsSupplierFactory annotationsSupplierFactory) {
        super(typeResolverSupport);
        this.annotationsSupplierFactory = annotationsSupplierFactory;
    }

    @Nullable
    @Override
    protected RecursionKey resolveIfSupported(SchemaResolver.Mode mode, Schema schema, InitialType initialType, SchemaBuilderFromType schemaBuilderFromType) {
        JavaType contentType = initialType.getType().getContentType();
        ArraySchema arraySchemaAnnotation = initialType.getAnnotationsSupplier().findAnnotations(ArraySchema.class).findFirst().orElse(null);
        schemaBuilderFromType.buildSchemaFromType(contentType, createAnnotationsSupplierFromContentType(arraySchemaAnnotation, contentType), schema::setItems);
        return null; // collections never create cyclic dependencies
    }

    private AnnotationsSupplier createAnnotationsSupplierFromContentType(@Nullable ArraySchema arraySchemaAnnotation, JavaType contentType) {
        // do not use the current annotationsSupplier, but only use annotations directly present on contentType
        // and add possible schema annotations from the array schema annotation
        AnnotationsSupplier annotationsSupplierForRawClass = annotationsSupplierFactory.createFromAnnotatedElement(contentType.getRawClass());
        if (arraySchemaAnnotation != null) {
            AnnotationsSupplier annotationsSupplierFromArraySchema = new AnnotationsSupplier() {
                @Override
                public <A extends Annotation> Stream<A> findAnnotations(Class<A> annotationType) {
                    return Stream.of(arraySchemaAnnotation.schema())
                            .filter(annotationType::isInstance)
                            .map(annotationType::cast);
                }
            };
            return annotationsSupplierFromArraySchema.andThen(annotationsSupplierForRawClass);
        }
        return annotationsSupplierForRawClass;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
