package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

public interface NestedTypeSchemaResolver extends Ordered {
    boolean apply(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer);
}
