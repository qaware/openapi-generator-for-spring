package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;

import java.util.function.Consumer;

public interface SchemaAnnotationMapper {
    void buildFromAnnotation(
            io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
            ReferencedSchemaConsumer referencedSchemaConsumer,
            Consumer<Schema> schemaSetter
    );

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
