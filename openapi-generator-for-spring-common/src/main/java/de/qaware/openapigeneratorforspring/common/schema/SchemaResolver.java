package de.qaware.openapigeneratorforspring.common.schema;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaResolver {
    Schema<?> resolveFromClass(Class<?> clazz, NestedSchemaConsumer nestedSchemaConsumer);
}
