package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForObject;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.function.Consumer;

public class TypeResolverForObject implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, SchemaBuilderFromType schemaBuilderFromType, SchemaBuilderFromType recursiveSchemaBuilderFromType) {
        if (javaType.getRawClass().equals(Object.class)) {
            // accept object schema without any properties as fallback here
            // to prevent traversing to meaningless properties of "Object"
            schemaConsumer.accept(InitialTypeResolverForObject.OBJECT_SCHEMA_SUPPLIER.get());
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
