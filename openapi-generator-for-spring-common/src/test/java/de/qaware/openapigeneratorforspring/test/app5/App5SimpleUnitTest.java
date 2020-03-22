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
import de.qaware.openapigeneratorforspring.common.schema.DefaultReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.DefaultSchemaNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.GenericTypeResolverForCollections;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.GenericTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.GenericTypeResolverForReferenceType;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.SimpleTypeResolverForObject;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.SimpleTypeResolverForPrimitiveTypes;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(ContainerDto.class));
        System.out.println(MAPPER.writeValueAsString(resolvedSchema.schema));
    }

    @Test
    public void resolveSchemaWithResolver() throws Exception {

        DefaultReferenceNameFactory referenceNameFactory = new DefaultReferenceNameFactory();
        DefaultReferenceNameConflictResolver referenceNameConflictResolver = new DefaultReferenceNameConflictResolver();

        SchemaAnnotationMapper schemaAnnotationMapper = (schema, schemaAnnotation, referencedSchemaStorage) -> {
            // just for testing
            setStringIfNotBlank(schemaAnnotation.description(), schema::setDescription);
            setStringIfNotBlank(schemaAnnotation.title(), schema::setTitle);
            if (schemaAnnotation.nullable()) {
                schema.setNullable(true);
            }
        };

        SchemaResolver sut = new DefaultSchemaResolver(
                () -> MAPPER, schemaResolver -> schemaAnnotationMapper,
                new DefaultAnnotationsSupplierFactory(),
                Arrays.asList(new GenericTypeResolverForCollections(), new GenericTypeResolverForReferenceType(), new GenericTypeResolverForObject()),
                // order is important here
                Arrays.asList(new SimpleTypeResolverForPrimitiveTypes(), new SimpleTypeResolverForObject(new DefaultSchemaNameFactory())));

        ReferencedSchemaStorage storage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver);
        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(storage);

        io.swagger.v3.oas.models.media.Schema<?> schema = sut.resolveFromClass(Object.class, referencedSchemaConsumer);

        Map<ReferenceName, de.qaware.openapigeneratorforspring.common.schema.Schema> referencedSchemas = storage.buildReferencedSchemas();

        System.out.println("==== Constructed schema");
        System.out.println(MAPPER.writeValueAsString(schema));
        System.out.println("==== Referenced schemas");
        System.out.println(MAPPER.writeValueAsString(referencedSchemas.entrySet()));

    }

    @Schema(description = "global description", title = "should never appear")
    private interface BaseForEverything {

    }

    @Schema(title = "global title")
    private interface BaseForSomeDto extends BaseForEverything {

    }

    @Value
    private static class ContainerDto implements BaseForSomeDto {
        @Schema(nullable = true)
        Optional<String> stringOptional;
        AtomicReference<String> stringAtomicReference;

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
    private static class SimpleDto implements BaseForSomeDto {
        @Nullable
        String property1;
    }

    @Value
    private static class SomeDto implements BaseForSomeDto {
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
