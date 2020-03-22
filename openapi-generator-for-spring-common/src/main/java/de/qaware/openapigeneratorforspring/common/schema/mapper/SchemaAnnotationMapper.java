package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.Schema;

import javax.annotation.Nullable;

public interface SchemaAnnotationMapper {


    @Nullable
    default Schema buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer) {
        Schema schema = new Schema();
        applyFromAnnotation(schema, schemaAnnotation, referencedSchemaConsumer);
        // do not return anything if schema is still empty
        if (new Schema().equals(schema)) {
            return null;
        }
        return schema;
    }

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
