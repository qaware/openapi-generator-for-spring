package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaTypeResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForFlux implements InitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver fallbackResolver) {
        if (javaType.getRawClass().equals(Flux.class)) {
            return InitialSchema.builder().schema(Schema.builder().type("array").build()).build();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
