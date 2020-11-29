package de.qaware.openapigeneratorforspring.common.schema.mapper;

import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.model.media.Schema;

/**
 * Mapper for {@link io.swagger.v3.oas.annotations.media.Schema Schema annotation}.
 *
 * <p>Created by {@link SchemaAnnotationMapperFactory}.
 */
public interface SchemaAnnotationMapper {
    Schema buildFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);

    void applyFromAnnotation(Schema schema, io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, ReferencedSchemaConsumer referencedSchemaConsumer);
}
