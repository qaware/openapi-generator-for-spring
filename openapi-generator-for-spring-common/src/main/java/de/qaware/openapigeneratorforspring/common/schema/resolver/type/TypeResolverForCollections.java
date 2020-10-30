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
            schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema::setItems);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
