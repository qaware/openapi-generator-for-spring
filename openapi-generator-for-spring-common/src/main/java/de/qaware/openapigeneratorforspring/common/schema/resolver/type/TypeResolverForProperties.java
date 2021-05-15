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
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer.SchemaPropertyCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaProperty;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForObject;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@Slf4j
public class TypeResolverForProperties extends AbstractTypeResolver {

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = laterThan(DEFAULT_ORDER);

    private final List<SchemaPropertiesResolver> schemaPropertiesResolvers;
    private final List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers;

    public TypeResolverForProperties(
            InitialSchemaBuilderForObject initialSchemaBuilderForObject,
            List<SchemaPropertiesResolver> schemaPropertiesResolvers,
            List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers
    ) {
        super(initialSchemaBuilderForObject);
        this.schemaPropertiesResolvers = schemaPropertiesResolvers;
        this.schemaPropertiesCustomizers = schemaPropertiesCustomizers;
    }

    @Override
    @Nullable
    public RecursionKey resolveIfSupported(
            SchemaResolver.Mode mode,
            Schema schema,
            JavaType javaType,
            AnnotationsSupplier annotationsSupplier,
            SchemaBuilderFromType schemaBuilderFromType
    ) {

        Map<String, SchemaProperty> properties = OpenApiMapUtils.buildStringMapFromStream(
                schemaPropertiesResolvers.stream()
                        .flatMap(resolver -> resolver.findProperties(javaType, annotationsSupplier, mode).entrySet().stream()),
                Map.Entry::getKey,
                Map.Entry::getValue
        );

        Map<String, PropertyCustomizer> propertyCustomizers = buildPropertyCustomizers(schema, javaType, annotationsSupplier, properties.keySet());

        properties.forEach((propertyName, property) -> {
            PropertyCustomizer propertyCustomizer = propertyCustomizers.get(propertyName);
            schemaBuilderFromType.buildSchemaFromType(property.getType(), property.getAnnotationsSupplier(),
                    propertySchema -> schema.setProperty(propertyName,
                            propertyCustomizer.customize(propertySchema, property.getType(), property.getAnnotationsSupplier())
                    )
            );
        });

        return new UniqueSchemaKey(javaType, schema.hashCode());
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class UniqueSchemaKey implements RecursionKey {
        private final JavaType javaType;
        private final int schemaHash;
    }

    private Map<String, PropertyCustomizer> buildPropertyCustomizers(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Set<String> propertyNames) {
        Map<String, PropertyCustomizer> customizerProperties = buildStringMapFromStream(
                propertyNames.stream(),
                x -> x,
                ignored -> new PropertyCustomizer()
        );
        // capture the customization callbacks already here
        schemaPropertiesCustomizers.forEach(customizer -> customizer.customize(schema, javaType, annotationsSupplier, customizerProperties));
        return customizerProperties;
    }

    @RequiredArgsConstructor
    private static class PropertyCustomizer implements SchemaPropertiesCustomizer.SchemaProperty {

        private final List<SchemaPropertyCustomizer> schemaPropertyCustomizers = new ArrayList<>();

        @Override
        public void customize(SchemaPropertyCustomizer propertySchemaCustomizer) {
            this.schemaPropertyCustomizers.add(propertySchemaCustomizer);
        }

        public Schema customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            schemaPropertyCustomizers.forEach(customizer -> customizer.customize(propertySchema, javaType, annotationsSupplier));
            // avoid running customizers multiple again when referenced schemas are consumed
            schemaPropertyCustomizers.clear();
            return propertySchema;
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
