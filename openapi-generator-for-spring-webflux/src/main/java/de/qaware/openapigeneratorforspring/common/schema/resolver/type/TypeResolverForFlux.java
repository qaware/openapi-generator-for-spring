package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

public class TypeResolverForFlux implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    @Nullable
    public RecursionKey resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        if (javaType.getRawClass().equals(Flux.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            Schema schema = initialSchema.getSchema();
            schemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, schema::setItems);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
