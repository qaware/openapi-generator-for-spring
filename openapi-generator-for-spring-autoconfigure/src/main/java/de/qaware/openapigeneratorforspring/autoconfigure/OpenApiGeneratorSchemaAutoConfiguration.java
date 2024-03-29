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
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForDeprecated;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForRequiredProperties;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaPropertiesCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.DefaultSchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilter;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilterForIgnoredMembers;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilterForNamelessMembers;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollectionLikeTypeSupport;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForMapLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForEnum;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForPrimitiveTypes;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForVoid;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilderForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilderForSchemaAnnotation;
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
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<InitialTypeBuilder> initialTypeBuilders,
            List<InitialSchemaBuilder> initialSchemaBuilders,
            List<SchemaCustomizer> schemaCustomizers,
            List<TypeResolver> typeResolvers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, annotationsSupplierFactory,
                initialTypeBuilders, initialSchemaBuilders, schemaCustomizers, typeResolvers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForCollectionLikeTypeSupport defaultTypeResolverForCollectionLikeTypeSupport(
            AnnotationsSupplierFactory annotationsSupplierFactory,
            ExtensionAnnotationMapper extensionAnnotationMapper,
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier
    ) {
        return new TypeResolverForCollectionLikeTypeSupport(annotationsSupplierFactory, extensionAnnotationMapper, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForCollectionLikeType defaultTypeResolverForCollectionLikeType(
            TypeResolverForCollectionLikeTypeSupport typeResolverForCollectionLikeTypeSupport
    ) {
        return new TypeResolverForCollectionLikeType(typeResolverForCollectionLikeTypeSupport);
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForMapLikeType defaultTypeResolverForMapLikeType(SchemaNameBuilder schemaNameBuilder) {
        return new TypeResolverForMapLikeType(schemaNameBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForObject defaultTypeResolverForObject(
            SchemaNameBuilder schemaNameBuilder,
            List<SchemaPropertiesResolver> schemaPropertiesResolvers,
            List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers,
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier
    ) {
        return new TypeResolverForObject(schemaNameBuilder, schemaPropertiesResolvers,
                schemaPropertiesCustomizers, openApiObjectMapperSupplier);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialTypeBuilderForReferenceType defaultInitialTypeBuilderForReferenceType(
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new InitialTypeBuilderForReferenceType(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialTypeBuilderForSchemaAnnotation defaultInitialTypeBuilderForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new InitialTypeBuilderForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
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
    public InitialSchemaBuilderForEnum defaultInitialSchemaBuilderForEnum(SchemaNameBuilder schemaNameBuilder) {
        return new InitialSchemaBuilderForEnum(schemaNameBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForSchemaAnnotation defaultSchemaCustomizerForSchemaAnnotation(
            ParsableValueMapper parsableValueMapper,
            ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper,
            ExtensionAnnotationMapper extensionAnnotationMapper
    ) {
        return new SchemaCustomizerForSchemaAnnotation(parsableValueMapper, externalDocumentationAnnotationMapper, extensionAnnotationMapper);
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
    public DefaultSchemaPropertiesResolver defaultSchemaPropertiesResolver(
            OpenApiObjectMapperSupplier objectMapperSupplier,
            List<SchemaPropertyFilter> schemaPropertyFilters,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new DefaultSchemaPropertiesResolver(objectMapperSupplier, schemaPropertyFilters, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaPropertyFilterForIgnoredMembers defaultSchemaPropertyFilterForIgnoredMembers() {
        return new SchemaPropertyFilterForIgnoredMembers();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaPropertyFilterForNamelessMembers defaultSchemaPropertyFilterForNamelessMembers() {
        return new SchemaPropertyFilterForNamelessMembers();
    }
}
