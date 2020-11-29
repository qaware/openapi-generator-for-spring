package de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.spring;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import org.springframework.http.ResponseEntity;

import javax.annotation.Nullable;

public class SpringWebResponseEntityTypeResolver implements InitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        if (javaType.getRawClass().equals(ResponseEntity.class)) {
            return resolver.resolveFromType(javaType.containedType(0), annotationsSupplier);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


}
