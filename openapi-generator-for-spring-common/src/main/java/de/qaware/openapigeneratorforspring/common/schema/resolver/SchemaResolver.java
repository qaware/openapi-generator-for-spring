package de.qaware.openapigeneratorforspring.common.schema.resolver;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;

import java.lang.reflect.Type;

public interface SchemaResolver {
    Schema resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer);

    Schema resolveFromClass(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer);
}
