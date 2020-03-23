package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;

import javax.annotation.Nullable;

public interface SchemaAnnotationMapper {
    @Nullable
    Schema buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
