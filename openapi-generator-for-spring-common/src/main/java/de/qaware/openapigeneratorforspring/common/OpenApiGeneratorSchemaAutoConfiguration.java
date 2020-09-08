package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolverForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.SimpleTypeResolverForPrimitiveTypes;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorAnnotationAutoConfiguration.class,
        OpenApiGeneratorUtilAutoConfiguration.class,
        OpenApiGeneratorSchemaMapperFactoryAutoConfiguration.class
})
public class OpenApiGeneratorSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchemaResolver defaultSchemaResolver(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            SchemaAnnotationMapperFactory schemaAnnotationMapperFactory,
            AnnotationsSupplierFactory annotationsSupplierFactory,
            List<GenericTypeResolver> genericTypeResolvers,
            List<SimpleTypeResolver> simpleTypeResolvers,
            List<SchemaCustomizer> schemaCustomizers
    ) {
        return new DefaultSchemaResolver(
                openApiObjectMapperSupplier, schemaAnnotationMapperFactory, annotationsSupplierFactory,
                genericTypeResolvers, simpleTypeResolvers, schemaCustomizers
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForCollections defaultGenericTypeResolverForCollections() {
        return new GenericTypeResolverForCollections();
    }


    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForReferenceType defaultGenericTypeResolverForReferenceType() {
        return new GenericTypeResolverForReferenceType();
    }


    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForObject defaultGenericTypeResolverForObject() {
        return new GenericTypeResolverForObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public GenericTypeResolverForSchemaAnnotation defaultGenericTypeResolverForSchemaAnnotation(
            OpenApiObjectMapperSupplier openApiObjectMapperSupplier,
            AnnotationsSupplierFactory annotationsSupplierFactory
    ) {
        return new GenericTypeResolverForSchemaAnnotation(openApiObjectMapperSupplier, annotationsSupplierFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleTypeResolverForPrimitiveTypes defaultSimpleTypeResolverForPrimitiveTypes() {
        return new SimpleTypeResolverForPrimitiveTypes();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleTypeResolverForObject defaultSimpleTypeResolverForObject(SchemaNameFactory schemaNameFactory) {
        return new SimpleTypeResolverForObject(schemaNameFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaCustomizerForNullable defaultSchemaCustomizerForNullable() {
        return new SchemaCustomizerForNullable();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaNameFactory defaultSchemaNameFactory() {
        return new DefaultSchemaNameFactory();
    }

}
