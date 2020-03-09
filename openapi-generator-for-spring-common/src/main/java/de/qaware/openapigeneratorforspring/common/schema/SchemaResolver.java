package de.qaware.openapigeneratorforspring.common.schema;

public interface SchemaResolver {
    Schema resolveFromClass(Class<?> clazz, NestedSchemaConsumer nestedSchemaConsumer);
}
