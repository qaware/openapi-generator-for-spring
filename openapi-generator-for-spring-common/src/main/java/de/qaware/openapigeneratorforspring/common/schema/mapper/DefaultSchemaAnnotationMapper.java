package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.media.Discriminator;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultSchemaAnnotationMapper implements SchemaAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final SchemaResolver schemaResolver;

    @Override
    public void buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
                                    ReferencedSchemaConsumer referencedSchemaConsumer,
                                    Consumer<Schema> schemaSetter) {
        Schema schema;
        if (!Void.class.equals(schemaAnnotation.implementation())) {
            // reference tracking will be done once the annotation is applied
            schema = schemaResolver.resolveFromClassWithoutReference(schemaAnnotation.implementation(), referencedSchemaConsumer);
        } else {
            schema = Schema.builder().build();
        }

        applyFromAnnotation(schema, schemaAnnotation, referencedSchemaConsumer);

        if (schema.isEmpty()) {
            return;
        }

        referencedSchemaConsumer.maybeAsReference(schema, schemaSetter);
    }

    @Override
    public void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema annotation, ReferencedSchemaConsumer referencedSchemaConsumer) {

        setStringIfNotBlank(annotation.name(), schema::setName);
        setStringIfNotBlank(annotation.title(), schema::setTitle);
        setStringIfNotBlank(annotation.description(), schema::setDescription);
        setStringIfNotBlank(annotation.format(), schema::setFormat);
        setStringIfNotBlank(annotation.ref(), schema::setRef);

        if (annotation.nullable()) {
            schema.setNullable(true);
        }
        setCollectionIfNotEmpty(Arrays.asList(annotation.requiredProperties()), schema::setRequired);
        setAccessMode(annotation.accessMode(), schema);
        setStringIfNotBlank(annotation.example(), example -> schema.setExample(parsableValueMapper.parse(example)));
        setIfNotEmpty(externalDocumentationAnnotationMapper.map(annotation.externalDocs()), schema::setExternalDocs);
        if (annotation.deprecated()) {
            schema.setDeprecated(true);
        }
        setStringIfNotBlank(annotation.type(), schema::setType);

        List<Object> allowableValues = Stream.of(annotation.allowableValues())
                .map(parsableValueMapper::parse)
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(allowableValues, schema::set_enum);

        setStringIfNotBlank(annotation.defaultValue(), value -> schema.set_default(parsableValueMapper.parse(value)));

        setDiscriminator(schema, annotation, referencedSchemaConsumer);

        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), schema::setExtensions);
    }

    private void setDiscriminator(Schema schema, io.swagger.v3.oas.annotations.media.Schema annotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
        String propertyName = annotation.discriminatorProperty();
        DiscriminatorMapping[] mappings = annotation.discriminatorMapping();
        if (StringUtils.isBlank(propertyName) || ArrayUtils.isEmpty(mappings)) {
            return;
        }

        Map<String, String> schemaReferenceMapping = new LinkedHashMap<>();
        schema.setDiscriminator(
                Discriminator.builder()
                        .propertyName(propertyName)
                        .mapping(schemaReferenceMapping)
                        .build()
        );

        for (DiscriminatorMapping mapping : mappings) {
            Schema mappingSchema = schemaResolver.resolveFromClassWithoutReference(mapping.schema(), referencedSchemaConsumer);
            referencedSchemaConsumer.alwaysAsReference(mappingSchema, schemaReference -> schemaReferenceMapping.put(mapping.value(), schemaReference.getRef()));
        }
    }

    private void setAccessMode(AccessMode accessMode, Schema schema) {
        switch (accessMode) {
            case READ_ONLY:
                schema.setReadOnly(true);
                break;
            case WRITE_ONLY:
                schema.setWriteOnly(true);
                break;
            case READ_WRITE:
            case AUTO:
                break;
        }
    }
}
