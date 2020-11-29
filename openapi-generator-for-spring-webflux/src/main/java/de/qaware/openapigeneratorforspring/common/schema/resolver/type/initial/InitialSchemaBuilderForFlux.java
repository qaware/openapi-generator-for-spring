package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForFlux implements InitialSchemaBuilder, TypeResolverSupport {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        if (javaType.getRawClass().equals(Flux.class)) {
            return InitialSchema.builder().schema(new FluxSchema()).build();
        }
        return null;
    }

    @EqualsAndHashCode(callSuper = true)
    private static class FluxSchema extends Schema {
        public FluxSchema() {
            setType("array");
        }
    }

    @Override
    public boolean supports(InitialSchema initialSchema) {
        return initialSchema.getSchema() instanceof FluxSchema;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
