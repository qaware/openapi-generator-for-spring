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

package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;

public class SchemaCustomizerForRequiredProperties implements SchemaPropertiesCustomizer {
    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaProperty> properties) {
        properties.forEach((propertyName, customizer) ->
                customizer.customize((propertySchema, propertyJavaType, propertyAnnotationsSupplier) -> {
                    propertyAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                            .findFirst()
                            .map(io.swagger.v3.oas.annotations.media.Schema::required)
                            .filter(flag -> flag)
                            .ifPresent(ignored -> schema.addRequired(propertyName));
                })
        );
    }
}
