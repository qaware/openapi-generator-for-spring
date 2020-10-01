package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

public class DefaultSpringResponseEntityTypeResolver implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

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
        return ORDER;
    }
}
