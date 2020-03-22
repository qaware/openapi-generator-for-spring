package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

import java.util.function.Consumer;

@FunctionalInterface
public interface SchemaBuilderFromType {
    void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer);
}