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
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.earlierThan;

@RequiredArgsConstructor
public class InitialTypeBuilderForSchemaAnnotation implements InitialTypeBuilder {

    // make this higher precedence as the implementation from the @Schema annotation should override anything else
    public static final int ORDER = earlierThan(DEFAULT_ORDER);

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Nullable
    @Override
    public InitialType build(SchemaResolver.Caller caller, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveBuilder recursiveBuilder) {
        return annotationsSupplier.findAnnotations(Schema.class)
                .map(Schema::implementation)
                .filter(clazz -> !Void.class.equals(clazz))
                .findFirst()
                .map(clazz -> recursiveBuilder.build(
                        openApiObjectMapperSupplier.get(SCHEMA_BUILDING).constructType(clazz),
                        annotationsSupplierFactory.createFromAnnotatedElement(clazz)
                ))
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
