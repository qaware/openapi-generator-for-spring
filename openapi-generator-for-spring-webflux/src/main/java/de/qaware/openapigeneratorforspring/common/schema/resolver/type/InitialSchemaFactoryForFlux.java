package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;

public class InitialSchemaFactoryForFlux implements InitialSchemaFactory {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
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
