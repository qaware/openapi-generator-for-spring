package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class SimpleTypeResolverForObject implements SimpleTypeResolver {

    public static final Supplier<Schema> OBJECT_SCHEMA_SUPPLIER = () -> new Schema().type("object");
    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    private final SchemaNameFactory schemaNameFactory;

    @Nullable
    @Override
    public Schema resolveFromType(JavaType javaType) {
        Schema schema = OBJECT_SCHEMA_SUPPLIER.get();
        schema.setName(schemaNameFactory.create(javaType));
        return schema;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
