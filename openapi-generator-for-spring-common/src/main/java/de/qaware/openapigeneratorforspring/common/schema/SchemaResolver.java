package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

import java.lang.reflect.Type;

public interface SchemaResolver {
    Schema resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer);

    Schema resolveFromClass(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer);
}
