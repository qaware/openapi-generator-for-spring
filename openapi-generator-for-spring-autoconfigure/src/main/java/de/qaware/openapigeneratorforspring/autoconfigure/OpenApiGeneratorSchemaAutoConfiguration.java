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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForProperties;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForPrimitiveTypes;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorSchemaTypeExtensionAutoConfiguration.class
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
    public TypeResolverForCollections defaultTypeResolverForCollections() {
        return new TypeResolverForCollections();
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForProperties defaultTypeResolverForProperties(
            List<SchemaPropertiesCustomizer> schemaPropertiesCustomizers,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new TypeResolverForProperties(schemaPropertiesCustomizers, annotationsSupplierFactory);
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
