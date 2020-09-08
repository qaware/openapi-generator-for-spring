package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Simple type resolver hiding away the recursive nature of {@link TypeResolver}
 * by requiring only {@link #resolveSchemaFromType} to be implemented.
 */
public interface SimpleTypeResolver extends TypeResolver {

    @Override
    default boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        Schema schema = resolveSchemaFromType(javaType, annotationsSupplier);
        if (schema != null) {
            schemaConsumer.accept(schema);
            return true;
        }
        return false;
    }

    @Nullable
    Schema resolveSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier);
}
