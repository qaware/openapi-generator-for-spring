package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaBuilderFromType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class TypeResolverForMonoAndFlux implements TypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Override
    public boolean resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, SchemaBuilderFromType schemaBuilderFromType, SchemaBuilderFromType recursiveSchemaBuilderFromType) {
        if (javaType.getRawClass().equals(Mono.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            recursiveSchemaBuilderFromType.buildSchemaFromType(innerType, annotationsSupplier, schemaConsumer);
            return true;
        } else if (javaType.getRawClass().equals(Flux.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            TypeResolverForCollections.continueWithInnerType(innerType, annotationsSupplier, recursiveSchemaBuilderFromType, schemaConsumer);
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
