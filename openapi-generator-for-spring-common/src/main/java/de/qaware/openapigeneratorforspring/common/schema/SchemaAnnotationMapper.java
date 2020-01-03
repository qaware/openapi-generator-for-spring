package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaAnnotationMapper {
    Schema<Object> mapFromAnnotation(io.swagger.v3.oas.annotations.media.Schema schemaAnnotation, NestedSchemaConsumer nestedSchemaConsumer);
}
