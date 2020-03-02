package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaAnnotationMapper {
    default Schema<Object> buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, NestedSchemaConsumer nestedSchemaConsumer) {
        Schema<Object> schema = new Schema<>();
        applyFromAnnotation(schema, schemaAnnotation, nestedSchemaConsumer);
        return schema;
    }

    void applyFromAnnotation(Schema<Object> schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, NestedSchemaConsumer nestedSchemaConsumer);
}
