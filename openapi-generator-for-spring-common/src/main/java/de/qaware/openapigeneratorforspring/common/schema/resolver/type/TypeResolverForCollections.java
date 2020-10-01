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
            continueWithInnerType(javaType.getContentType(), annotationsSupplier, schemaBuilderFromType, schemaConsumer);
            return true;
        }
        return false;
    }

    static void continueWithInnerType(JavaType innerType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType, Consumer<Schema> schemaConsumer) {
        // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema?
        Schema arraySchema = new Schema().type("array");
        // TODO append annotationSupplier with contained generic type!
        schemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, schema -> {
                    arraySchema.setItems(schema);
                    schemaConsumer.accept(arraySchema);
                }
        );
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
