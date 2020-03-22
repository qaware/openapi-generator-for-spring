package de.qaware.openapigeneratorforspring.test.app5;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.DefaultReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.annotation.DefaultAnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.schema.annotation.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.typeresolver.GenericTypeResolverForCollections;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class App5SimpleUnitTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);


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
        };

        SchemaResolver sut = new DefaultSchemaResolver(
                () -> MAPPER, schemaResolver -> schemaAnnotationMapper,
                new DefaultAnnotationsSupplierFactory(),
                Collections.singletonList(new GenericTypeResolverForCollections()),
                // order is important here
                Arrays.asList(new SimpleTypeResolverForPrimitiveTypes(), new SimpleTypeResolverForObject()));

        ReferencedSchemaStorage storage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver);
        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(storage);

        io.swagger.v3.oas.models.media.Schema<?> schema = sut.resolveFromClass(SomeDto.class, referencedSchemaConsumer);

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
        List<String> property1;
        Map<String, String> property2;
        List<SimpleDto> property3;
        Map<String, SimpleDto> property4;
        List<List<String>> property5;
        Map<SimpleDto, SimpleDto> property6;
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
