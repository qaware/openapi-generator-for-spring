package de.qaware.openapigeneratorforspring.test.app8;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Consumer;

@Component
public class InstantGenericTypeResolver implements GenericTypeResolver {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.getRawClass().equals(Instant.class)) {
            schemaConsumer.accept(new Schema().type("string").format("ISO8601"));
            return true;
        } else {
            return false;
        }
    }
}
