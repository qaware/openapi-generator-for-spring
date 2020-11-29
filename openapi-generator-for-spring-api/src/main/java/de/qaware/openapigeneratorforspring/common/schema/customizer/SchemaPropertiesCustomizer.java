/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;

/**
 * Customizer for {@link Schema} properties. As properties
 * are built recursively, this interface is not a simple
 * call to each property. This allows to modify the parent
 * schema, containing the properties, to be modified as well.
 *
 * <p>Prefer using {@link SchemaCustomizer} whenever possible
 */
@FunctionalInterface
public interface SchemaPropertiesCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize the given properties (map from name to customizer callback)
     *
     * @param schema              schema owning the given properties
     * @param javaType            java type of owning schema
     * @param annotationsSupplier annotations supplier of owning schema
     * @param properties          map of property name to property customizer callback
     */
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier,
                   Map<String, ? extends SchemaProperty> properties);

    /**
     * Customizer callback for property. Helper interface to provide
     * properties map to {@link SchemaPropertiesCustomizer#customize}.
     */
    @FunctionalInterface
    interface SchemaProperty {
        /**
         * Customizer callback which is called once the property schema is eventually built.
         *
         * @param schemaPropertyCustomizer customizer for schema of property
         */
        void customize(SchemaPropertyCustomizer schemaPropertyCustomizer);
    }

    /**
     * Customizer for schema property.
     */
    @FunctionalInterface
    interface SchemaPropertyCustomizer {
        /**
         * Customize schema of property.
         *
         * @param propertySchema      schema of property
         * @param javaType            java type of property (never Void)
         * @param annotationsSupplier annotations supplier for property (member)
         */
        void customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }
}
