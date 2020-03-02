package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaResolver {
    Schema<Object> resolveFromClass(Class<?> clazz, NestedSchemaConsumer nestedSchemaConsumer);
}
