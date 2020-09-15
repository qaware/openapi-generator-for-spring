package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.models.media.Discriminator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
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
            schema = new Schema();
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
        setStringIfNotBlank(annotation.ref(), schema::set$ref);

        if (annotation.nullable()) {
            schema.setNullable(true);
        }
        setAccessMode(annotation.accessMode(), schema);
        setStringIfNotBlank(annotation.example(), example -> schema.setExample(parsableValueMapper.parse(example)));
        externalDocumentationAnnotationMapper.map(annotation.externalDocs())
                .ifPresent(schema::setExternalDocs);
        if (annotation.deprecated()) {
            schema.setDeprecated(true);
        }
        setStringIfNotBlank(annotation.type(), schema::setType);

        List<Object> allowableValues = Stream.of(annotation.allowableValues())
                .map(parsableValueMapper::parse)
                .collect(Collectors.toList());
        // TODO fix generic problem here
        setCollectionIfNotEmpty(allowableValues, schema::setEnum);

        setStringIfNotBlank(annotation.defaultValue(), value -> schema.setDefault(parsableValueMapper.parse(value)));

        setDiscriminator(schema, annotation, referencedSchemaConsumer);

        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), schema::setExtensions);
    }

    private void setDiscriminator(Schema schema, io.swagger.v3.oas.annotations.media.Schema annotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
        String propertyName = annotation.discriminatorProperty();
        DiscriminatorMapping[] mappings = annotation.discriminatorMapping();
        if (StringUtils.isBlank(propertyName) || ArrayUtils.isEmpty(mappings)) {
            return;
        }

        Map<String, Schema> schemasMap = OpenApiMapUtils.buildMapFromArray(
                mappings,
                DiscriminatorMapping::value,
                // cannot reference already here as discriminator mappings must always be referenced
                mapping -> schemaResolver.resolveFromClassWithoutReference(mapping.schema(), referencedSchemaConsumer)
        );

        Discriminator discriminator = new Discriminator()
                .propertyName(propertyName);

        referencedSchemaConsumer.alwaysAsReferences(
                schemasMap.entrySet().stream()
                        .map(entry -> ReferencedSchemaConsumer.EntryWithSchema.of(entry, entry.getValue())),
                entriesWithReferenceNames -> discriminator.setMapping(
                        entriesWithReferenceNames.collect(Collectors.toMap(
                                entry -> entry.getReferenceName().asReferenceString(),
                                entry -> entry.getEntry().getKey()
                        ))
                )
        );

        schema.setDiscriminator(discriminator);
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
