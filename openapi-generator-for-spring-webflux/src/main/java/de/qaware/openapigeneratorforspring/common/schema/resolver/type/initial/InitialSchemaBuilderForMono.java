package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForMono implements InitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        if (javaType.getRawClass().equals(Mono.class)) {
            JavaType innerType = javaType.getBindings().getTypeParameters().iterator().next();
            return resolver.resolveFromType(innerType, annotationsSupplier);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
