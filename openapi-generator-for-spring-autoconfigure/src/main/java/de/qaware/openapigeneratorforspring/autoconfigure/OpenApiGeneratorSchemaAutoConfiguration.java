/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Autoconfigure
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

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForDeprecated;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForRequiredProperties;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.DefaultSchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilter;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilterForIgnoredMembers;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForProperties;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForEnum;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForPrimitiveTypes;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForVoid;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorSchemaExtensionAutoConfiguration.class
})
public class OpenApiGeneratorSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchemaResolver defaultSchemaResolver(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory,
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<TypeResolver> typeResolvers,
            List<InitialSchemaBuilder> initialSchemaBuilders,
            List<SchemaCustomizer> schemaCustomizers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, schemaAnnotationMapperFactory, annotationsSupplierFactory,
                typeResolvers, initialSchemaBuilders, schemaCustomizers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForCollectionLikeType defaultTypeResolverForCollectionLikeType(
            InitialSchemaBuilderForCollectionLikeType initialSchemaBuilder,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new TypeResolverForCollectionLikeType(initialSchemaBuilder, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForProperties defaultTypeResolverForProperties(
            InitialSchemaBuilderForObject initialSchemaBuilder,
            List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new TypeResolverForProperties(initialSchemaBuilder, schemaPropertiesCustomizers, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForReferenceType defaultInitialSchemaBuilderForReferenceType() {
        return new InitialSchemaBuilderForReferenceType();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForSchemaAnnotation defaultInitialSchemaBuilderForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new InitialSchemaBuilderForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForPrimitiveTypes defaultInitialSchemaBuilderForPrimitiveTypes() {
        return new InitialSchemaBuilderForPrimitiveTypes();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForVoid defaultInitialSchemaBuilderForVoid() {
        return new InitialSchemaBuilderForVoid();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForEnum defaultInitialSchemaBuilderForEnum() {
        return new InitialSchemaBuilderForEnum();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForObject defaultInitialSchemaBuilderForObject(SchemaNameBuilder schemaNameBuilder, SchemaPropertiesResolver schemaPropertiesResolver) {
        return new InitialSchemaBuilderForObject(schemaNameBuilder, schemaPropertiesResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaBuilderForCollectionLikeType defaultInitialSchemaBuilderForCollectionLikeType() {
        return new InitialSchemaBuilderForCollectionLikeType();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForNullable defaultSchemaCustomizerForNullable() {
        return new SchemaCustomizerForNullable();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForDeprecated defaultSchemaCustomizerForDeprecated() {
        return new SchemaCustomizerForDeprecated();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForRequiredProperties defaultSchemaCustomizerForRequiredProperties() {
        return new SchemaCustomizerForRequiredProperties();
    }


    @Bean
    @ConditionalOnMissingBean
    public SchemaNameBuilder defaultSchemaNameBuilder() {
        return new DefaultSchemaNameBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaPropertiesResolver defaultSchemaPropertiesResolver(
            OpenApiObjectMapperSupplier objectMapperSupplier,
            List<SchemaPropertyFilter> schemaPropertyFilters
    ) {
        return new DefaultSchemaPropertiesResolver(objectMapperSupplier, schemaPropertyFilters);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaPropertyFilterForIgnoredMembers defaultSchemaPropertyFilterForIgnoredMembers() {
        return new SchemaPropertyFilterForIgnoredMembers();
    }
}
