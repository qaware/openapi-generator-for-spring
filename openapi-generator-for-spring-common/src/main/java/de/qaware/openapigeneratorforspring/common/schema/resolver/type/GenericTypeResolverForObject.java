package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;

import java.util.function.Consumer;

public class GenericTypeResolverForObject implements GenericTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.getRawClass().equals(Object.class)) {
            // accept object schema without any properties as fallback here
            schemaConsumer.accept(SimpleTypeResolverForObject.OBJECT_SCHEMA_SUPPLIER.get());
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
