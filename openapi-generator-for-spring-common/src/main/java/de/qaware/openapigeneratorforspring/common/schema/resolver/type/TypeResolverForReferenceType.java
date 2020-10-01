package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.function.Consumer;

public class TypeResolverForReferenceType implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.isReferenceType()) {
            // TODO append annotationSupplier with contained generic type!
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema -> {
                if (schema.getNullable() == null) {
                    // TODO check if all jackson reference types should be considered @Nullable by default
                    schema.setNullable(true);
                }
                schemaConsumer.accept(schema);
            });
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
