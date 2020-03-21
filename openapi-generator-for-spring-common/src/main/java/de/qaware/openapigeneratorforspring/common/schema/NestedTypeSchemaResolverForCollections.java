package de.qaware.openapigeneratorforspring.common.schema;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.core.Ordered;

import java.util.function.Consumer;

public class NestedTypeSchemaResolverForCollections implements NestedTypeSchemaResolver {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;

    @Override
    public boolean apply(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.isCollectionLikeType()) {
            Schema containerSchema = new Schema();
            containerSchema.setType("array");
            // TODO adapt annotations supplier to content type, consider @ArraySchema
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, containerSchema::setItems);
            schemaConsumer.accept(containerSchema);
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
