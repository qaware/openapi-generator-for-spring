package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.SchemaBuilderFromType;

import java.util.function.Consumer;

public class GenericTypeResolverForCollections implements GenericTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
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
