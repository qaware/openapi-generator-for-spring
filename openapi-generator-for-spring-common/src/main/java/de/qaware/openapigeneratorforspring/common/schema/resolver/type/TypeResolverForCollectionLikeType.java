package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForCollectionLikeType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@Slf4j
public class TypeResolverForCollectionLikeType extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    public TypeResolverForCollectionLikeType(InitialSchemaBuilderForCollectionLikeType typeResolverSupport) {
        super(typeResolverSupport);
    }

    @Nullable
    @Override
    protected RecursionKey resolveInternal(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        // TODO adapt annotations supplier to nested getContentType, consider @ArraySchema?
        // TODO append annotationSupplier with contained generic type!
        Schema schema = initialSchema.getSchema();
        schemaBuilderFromType.buildSchemaFromType(javaType.getContentType(), annotationsSupplier, schema::setItems);
        return null; // collections never create cyclic dependencies
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
