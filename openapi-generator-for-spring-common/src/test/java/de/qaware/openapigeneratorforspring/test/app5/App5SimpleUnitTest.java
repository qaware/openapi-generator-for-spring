package de.qaware.openapigeneratorforspring.test.app5;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.qaware.openapigeneratorforspring.common.annotation.DefaultAnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizerForNullable;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForPrimitiveTypes;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class App5SimpleUnitTest {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();


    @Test
    public void resolveSchemaWithSwagger() throws Exception {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(SomeDto.class));
        System.out.println(MAPPER.writeValueAsString(resolvedSchema.schema));
    }

    @Test
    public void resolveSchemaWithResolver() throws Exception {

        DefaultReferenceNameFactory referenceNameFactory = new DefaultReferenceNameFactory();
        DefaultReferenceNameConflictResolver referenceNameConflictResolver = new DefaultReferenceNameConflictResolver();

        SchemaAnnotationMapper schemaAnnotationMapper = new SchemaAnnotationMapper() {
            @Nullable
            @Override
            public de.qaware.openapigeneratorforspring.common.schema.Schema buildFromAnnotation(Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
                // not used during schema resolution
                throw new NotImplementedException("not implemented");
            }

            @Override
            public void applyFromAnnotation(de.qaware.openapigeneratorforspring.common.schema.Schema schema, Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
                // just some simple stuff for testing
                setStringIfNotBlank(schemaAnnotation.description(), schema::setDescription);
                setStringIfNotBlank(schemaAnnotation.title(), schema::setTitle);
                if (schemaAnnotation.nullable()) {
                    schema.setNullable(true);
                }
            }
        };

        DefaultAnnotationsSupplierFactory annotationsSupplierFactory = new DefaultAnnotationsSupplierFactory();
        SchemaResolver sut = new DefaultSchemaResolver(
                () -> MAPPER, schemaResolver -> schemaAnnotationMapper,
                annotationsSupplierFactory,
                // order is important here
                Arrays.asList(
                        new TypeResolverForSchemaAnnotation(() -> MAPPER, annotationsSupplierFactory),
                        new TypeResolverForCollections(),
                        new TypeResolverForReferenceType(),
                        new TypeResolverForObject()
                ),
                Arrays.asList(
                        new InitialTypeResolverForPrimitiveTypes(),
                        new InitialTypeResolverForObject(new DefaultSchemaNameFactory())
                ),
                Arrays.asList(new SchemaCustomizerForNullable())
        );

        ReferencedSchemaStorage storage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver);
        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(storage);

        io.swagger.v3.oas.models.media.Schema<?> schema = sut.resolveFromClass(SimpleDto.class, referencedSchemaConsumer);

        Map<ReferenceName, de.qaware.openapigeneratorforspring.common.schema.Schema> referencedSchemas = storage.buildReferencedSchemas();

        System.out.println("==== Constructed schema");
        System.out.println(MAPPER.writeValueAsString(schema));
//        System.out.println("==== Referenced schemas");
//        System.out.println(MAPPER.writeValueAsString(referencedSchemas.entrySet()));

    }

    @Schema(description = "global description", title = "should never appear")
    private interface BaseForEverything {

    }

    @Schema(title = "global title")
    @Getter
    @Setter
    private abstract static class BaseForSomeDto implements BaseForEverything {
        private String propertyInBaseClass;
    }

    @Value
    private static class ContainerDto {
        @Schema(nullable = true)
        Optional<String> stringOptional;

//        AtomicReference<String> stringAtomicReference;

//        List<String> property1;
//        Map<String, String> property2;
//        List<SimpleDto> property3;
//        Map<String, SimpleDto> property4;
//        List<List<String>> property5;
//        Map<SimpleDto, SimpleDto> property6;
//        NestedDto nestedProperty;
//
//        @Value
//        private static class NestedDto {
//            NestedDto selfReferenceProperty;
//            String simpleNestedProperty1;
//        }
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    private static class SimpleDto extends BaseForSomeDto {
        @Nullable
        String property1;
        int someInteger;
        @Nullable
        Integer nullableInteger;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    private static class SomeDto extends BaseForSomeDto {
        List<String> strings;
        List<Set<String>> stringsOfStrings;
        List<Set<SomeDto>> listOfSetOfSomeDto;
        @Schema(description = "description1")
        SomeDto other1;
        @Schema(title = "title override", description = "description2")
        SomeDto other2;
        @Nullable
        SomeOtherDto someOtherDto;
    }

    @Value
    private static class SomeOtherDto {
        @Schema(description = "nestedDescription")
        SomeDto other;
        @Schema(description = "nestedOtherDescription")
        SomeOtherDto someOtherDto;
    }

}
