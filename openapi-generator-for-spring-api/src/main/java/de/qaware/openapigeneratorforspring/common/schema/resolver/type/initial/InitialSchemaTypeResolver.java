package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

import javax.annotation.Nullable;

@FunctionalInterface
public interface InitialSchemaTypeResolver {
    @Nullable
    InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier);
}
