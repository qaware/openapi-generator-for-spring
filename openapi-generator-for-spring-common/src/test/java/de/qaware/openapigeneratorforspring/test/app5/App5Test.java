package de.qaware.openapigeneratorforspring.test.app5;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.DefaultReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.schema.DefaultNestedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.DefaultSchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.NestedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.SchemaResolver;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class App5Test {

    @Test
    public void resolveSchemaWithSwagger() throws Exception {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance().readAllAsResolvedSchema(new AnnotatedType().type(SomeDto.class));
        System.out.println(resolvedSchema.schema);
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
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SchemaResolver sut = new DefaultSchemaResolver(() -> objectMapper, schemaAnnotationMapper);

        ReferencedSchemaStorage storage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolver);
        NestedSchemaConsumer nestedSchemaConsumer = new DefaultNestedSchemaConsumer(storage);

        io.swagger.v3.oas.models.media.Schema<Object> schema = sut.resolveFromClass(SomeDto.class, nestedSchemaConsumer);

        Map<ReferenceName, io.swagger.v3.oas.models.media.Schema<Object>> referencedSchemas = storage.buildReferencedSchemas();

        System.out.println("==== Constructed schema");
        System.out.println(objectMapper.writeValueAsString(schema));
        System.out.println("==== Referenced schemas");
        System.out.println(objectMapper.writeValueAsString(referencedSchemas.entrySet()));

    }

    @Schema(description = "global description", title = "should never appear")
    private interface BaseForEverything {

    }

    @Schema(title = "global title")
    private interface BaseForSomeDto extends BaseForEverything {

    }

    @Value
    private static class SimpleDto implements BaseForSomeDto {
        @Nullable
        String property1;
    }

    @Value
    private static class SomeDto implements BaseForSomeDto {
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
