package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;

import java.util.function.Consumer;

public class TypeResolverForCollections implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        if (javaType.isCollectionLikeType()) {
            // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema
            Schema containerSchema = new Schema();
            containerSchema.setType("array");
            // TODO append annotationSupplier with contained generic type!
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema -> {
                        containerSchema.setItems(schema);
                        schemaConsumer.accept(containerSchema);
                    }
            );
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
