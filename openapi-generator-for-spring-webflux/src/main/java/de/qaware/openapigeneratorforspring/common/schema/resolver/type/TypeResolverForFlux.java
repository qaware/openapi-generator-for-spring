package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForFlux;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

public class TypeResolverForFlux extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    public TypeResolverForFlux(InitialSchemaBuilderForFlux typeResolverSupport) {
        super(typeResolverSupport);
    }

    @Override
    @Nullable
    public RecursionKey resolveInternal(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
        Schema schema = initialSchema.getSchema();
        schemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, schema::setItems);
        return null; // Flux never creates cyclic schema dependencies
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
