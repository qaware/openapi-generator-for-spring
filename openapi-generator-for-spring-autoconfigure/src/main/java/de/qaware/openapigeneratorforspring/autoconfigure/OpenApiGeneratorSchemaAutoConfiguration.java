package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForDeprecated;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForRequiredProperties;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.DefaultSchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilter;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertyFilterForIgnoredMembers;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForProperties;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactoryForCollectionLikeType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactoryForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactoryForPrimitiveTypes;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactoryForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactoryForSchemaAnnotation;
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
            List<InitialSchemaFactory> initialSchemaFactories,
            List<SchemaCustomizer> schemaCustomizers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, schemaAnnotationMapperFactory, annotationsSupplierFactory,
                typeResolvers, initialSchemaFactories, schemaCustomizers
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
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new TypeResolverForProperties(annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaFactoryForReferenceType defaultInitialSchemaFactoryForReferenceType() {
        return new InitialSchemaFactoryForReferenceType();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaFactoryForSchemaAnnotation defaultInitialSchemaFactoryForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new InitialSchemaFactoryForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaFactoryForPrimitiveTypes defaultInitialSchemaFactoryForPrimitiveTypes() {
        return new InitialSchemaFactoryForPrimitiveTypes();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaFactoryForObject defaultInitialSchemaFactoryForObject(SchemaNameFactory schemaNameFactory, SchemaPropertiesResolver schemaPropertiesResolver) {
        return new InitialSchemaFactoryForObject(schemaNameFactory, schemaPropertiesResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialSchemaFactoryForCollectionLikeType defaultInitialSchemaFactoryForCollectionLikeType() {
        return new InitialSchemaFactoryForCollectionLikeType();
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
    public SchemaNameFactory defaultSchemaNameFactory() {
        return new DefaultSchemaNameFactory();
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
