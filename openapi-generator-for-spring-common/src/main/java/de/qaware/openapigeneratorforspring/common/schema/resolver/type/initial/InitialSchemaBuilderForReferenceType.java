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
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialSchemaBuilderForReferenceType implements InitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        if (javaType.isReferenceType()) {
            JavaType contentType = javaType.getContentType();
            return resolver.resolveFromType(contentType,
                    annotationsSupplier.andThen(annotationsSupplierFactory.createFromAnnotatedElement(contentType.getRawClass()))
            );
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
