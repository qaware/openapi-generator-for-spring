package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

public class InitialSchemaFactoryForMono implements InitialSchemaFactory {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
        if (javaType.getRawClass().equals(Mono.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            return fallbackResolver.resolveFromType(innerType, annotationsSupplier);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
