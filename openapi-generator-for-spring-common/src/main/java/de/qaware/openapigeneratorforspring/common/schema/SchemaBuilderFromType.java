package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.JavaType;

import java.util.function.Consumer;

@FunctionalInterface
public interface SchemaBuilderFromType {
    void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer);
}
