package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class TypeResolverForCollections implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public RecursionKey resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        if (javaType.isCollectionLikeType()) {
            // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema?
            // TODO append annotationSupplier with contained generic type!
            Schema schema = initialSchema.getSchema();
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, items -> {
                LOGGER.info("Setting items to {} for {}", items.toPrettyString(), schema.toPrettyString());
                schema.setItems(items);
            });
        }
        return null;
    }

//    @Override
//    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer,
//                                   SchemaBuilderFromType schemaBuilderFromType, SchemaBuilderFromType recursiveSchemaBuilderFromType) {
//        if (javaType.isCollectionLikeType()) {
//            continueWithInnerType(javaType.getContentType(), annotationsSupplier, recursiveSchemaBuilderFromType, schemaConsumer);
//            return true;
//        }
//        return false;
//    }

//    static void continueWithInnerType(JavaType innerType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType recursiveSchemaBuilderFromType, Consumer<Schema> schemaConsumer) {
//        // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema?
//        // TODO append annotationSupplier with contained generic type!
//
//        // modifying the captured arraySchema here is important for referencing later
//        // do not rebuild the array schema here everytime this schema consumer is run
//        Schema arraySchema = Schema.builder().type("array").build();
//        recursiveSchemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, innerTypeSchema -> {
//            arraySchema.setItems(innerTypeSchema);
//            schemaConsumer.accept(arraySchema);
//        });
//    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
