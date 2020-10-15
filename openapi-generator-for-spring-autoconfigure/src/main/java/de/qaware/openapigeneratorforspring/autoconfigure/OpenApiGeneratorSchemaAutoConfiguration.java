package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForDeprecated;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
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
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForPrimitiveTypes;
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
            SchemaPropertiesResolver schemaPropertiesResolver,
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory,
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<TypeResolver> typeResolvers,
            List<InitialTypeResolver> initialTypeResolvers,
            List<SchemaCustomizer> schemaCustomizers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, schemaPropertiesResolver, schemaAnnotationMapperFactory, annotationsSupplierFactory,
                typeResolvers, initialTypeResolvers, schemaCustomizers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForCollections defaultGenericTypeResolverForCollections() {
        return new TypeResolverForCollections();
    }


    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForReferenceType defaultGenericTypeResolverForReferenceType() {
        return new TypeResolverForReferenceType();
    }


    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForObject defaultGenericTypeResolverForObject() {
        return new TypeResolverForObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public TypeResolverForSchemaAnnotation defaultGenericTypeResolverForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new TypeResolverForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialTypeResolverForPrimitiveTypes defaultSimpleTypeResolverForPrimitiveTypes() {
        return new InitialTypeResolverForPrimitiveTypes();
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialTypeResolverForObject defaultSimpleTypeResolverForObject(SchemaNameFactory schemaNameFactory) {
        return new InitialTypeResolverForObject(schemaNameFactory);
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
