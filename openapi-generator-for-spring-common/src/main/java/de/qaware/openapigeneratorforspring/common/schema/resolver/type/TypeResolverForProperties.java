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
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForObject;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@Slf4j
public class TypeResolverForProperties extends AbstractTypeResolver {

    // this resolver does not have any condition, so run this always later then the other resolvers as a fallback
    public static final int ORDER = laterThan(DEFAULT_ORDER);

    private final List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public TypeResolverForProperties(InitialSchemaBuilderForObject initialSchemaBuilderForObject,
                                     List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers, AnnotationsSupplierFactory annotationsSupplierFactory) {
        super(initialSchemaBuilderForObject);
        this.schemaPropertiesCustomizers = schemaPropertiesCustomizers;
        this.annotationsSupplierFactory = annotationsSupplierFactory;
    }

    @Override
    @Nullable
    public RecursionKey resolveIfSupported(
            InitialSchema initialSchema,
            JavaType javaType,
            AnnotationsSupplier annotationsSupplier,
            SchemaBuilderFromType schemaBuilderFromType
    ) {
        Map<String, AnnotatedMember> properties = initialSchema.getProperties();
        Map<String, PropertyCustomizer> propertyCustomizers = buildPropertyCustomizers(initialSchema.getSchema(),
                javaType, annotationsSupplier, properties);

        properties.forEach((propertyName, member) -> {
            PropertyCustomizer propertyCustomizer = propertyCustomizers.get(propertyName);
            JavaType propertyType = member.getType();
            AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                    .andThen(annotationsSupplierFactory.createFromAnnotatedElement(propertyType.getRawClass()));
            schemaBuilderFromType.buildSchemaFromType(propertyType, propertyAnnotationsSupplier,
                    propertySchema -> initialSchema.getSchema().setProperty(propertyName, propertyCustomizer.customize(propertySchema, propertyType, propertyAnnotationsSupplier))
            );
        });

        return new UniqueSchemaKey(javaType, initialSchema.getSchema().hashCode());
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class UniqueSchemaKey implements RecursionKey {
        private final JavaType javaType;
        private final int schemaHash;
    }

    private Map<String, PropertyCustomizer> buildPropertyCustomizers(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, AnnotatedMember> properties) {
        Map<String, PropertyCustomizer> customizerProperties = buildStringMapFromStream(
                properties.entrySet().stream(),
                Map.Entry::getKey,
                entry -> new PropertyCustomizer()
        );
        schemaPropertiesCustomizers.forEach(customizer -> customizer.customize(schema, javaType, annotationsSupplier, customizerProperties));
        return customizerProperties;
    }

    @RequiredArgsConstructor
    private static class PropertyCustomizer implements SchemaPropertiesCustomizer.SchemaProperty {

        @Nullable
        private SchemaPropertiesCustomizer.SchemaPropertyCustomizer schemaPropertyCustomizer;

        @Override
        public void customize(SchemaPropertiesCustomizer.SchemaPropertyCustomizer propertySchemaCustomizer) {
            this.schemaPropertyCustomizer = propertySchemaCustomizer;
        }

        public Schema customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            if (schemaPropertyCustomizer != null) {
                schemaPropertyCustomizer.customize(propertySchema, javaType, annotationsSupplier);
                // TODO check if this is still necessary
                // avoid running customizers multiple again when referenced schemas are consumed
                schemaPropertyCustomizer = null;
            }
            return propertySchema;
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
