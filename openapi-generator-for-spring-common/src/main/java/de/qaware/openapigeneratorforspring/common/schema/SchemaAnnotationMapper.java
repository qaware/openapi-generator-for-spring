package de.qaware.openapigeneratorforspring.common.schema;

import javax.annotation.Nullable;

public interface SchemaAnnotationMapper {


    @Nullable
    default Schema buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, NestedSchemaConsumer nestedSchemaConsumer) {
        Schema schema = new Schema();
        applyFromAnnotation(schema, schemaAnnotation, nestedSchemaConsumer);
        // do not return anything if schema is still empty
        if (new Schema().equals(schema)) {
            return null;
        }
        return schema;
    }

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, NestedSchemaConsumer nestedSchemaConsumer);
}
