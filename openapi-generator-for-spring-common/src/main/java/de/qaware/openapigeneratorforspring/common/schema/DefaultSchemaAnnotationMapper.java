package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ParsableValueMapper;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultSchemaAnnotationMapper implements SchemaAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final SchemaResolver schemaResolver;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public Schema<Object> mapFromAnnotation(io.swagger.v3.oas.annotations.media.Schema annotation, NestedSchemaConsumer nestedSchemaConsumer) {
        Schema<Object> schema = new Schema<>();

        setStringIfNotBlank(annotation.name(), schema::setName);
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
        setCollectionIfNotEmpty(allowableValues, schema::setEnum);

        setStringIfNotBlank(annotation.defaultValue(), value -> schema.setDefault(parsableValueMapper.parse(value)));

        setDiscriminator(schema, annotation, nestedSchemaConsumer);

        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), schema::setExtensions);

        return schema;
    }

    private void setDiscriminator(Schema<Object> schema, io.swagger.v3.oas.annotations.media.Schema annotation, NestedSchemaConsumer nestedSchemaConsumer) {
        String propertyName = annotation.discriminatorProperty();
        DiscriminatorMapping[] mappings = annotation.discriminatorMapping();
        if (StringUtils.isBlank(propertyName) || ArrayUtils.isEmpty(mappings)) {
            return;
        }
        Map<String, String> mappingsMap = OpenApiMapUtils.buildMapFromArray(
                mappings,
                DiscriminatorMapping::value,
                mapping -> {
                    Schema<?> mappingSchema = schemaResolver.resolveFromClass(mapping.schema());
                    ReferenceName schemaReference = nestedSchemaConsumer.consumeAndCreateReference(mappingSchema);
                    return schemaReference.asUniqueString();
                }
        );
        schema.setDiscriminator(
                new Discriminator()
                        .propertyName(propertyName)
                        .mapping(mappingsMap)
        );
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
