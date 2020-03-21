package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SimpleTypeResolverForPrimitiveTypes implements SimpleTypeResolver {

    public static final Supplier<Schema> STRING_SCHEMA_SUPPLIER = () -> new Schema().type("string");

    public static final int ORDER = SimpleTypeResolverForObject.ORDER - 1000;

    @Nullable
    @Override
    public Schema resolveFromType(JavaType javaType) {
        // TODO do some more primitive type handling here
        if (javaType.getRawClass().equals(String.class)) {
            return STRING_SCHEMA_SUPPLIER.get();
        }

        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
