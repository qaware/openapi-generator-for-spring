package de.qaware.openapigeneratorforspring.test.app8;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ResponseEntityTypeResolver implements TypeResolver {
    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.getRawClass().equals(ResponseEntity.class)) {
            JavaType containedType = javaType.containedType(0);
            schemaBuilderFromType.buildSchemaFromType(containedType, annotationsSupplier, schemaConsumer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
