package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTypeResolver implements TypeResolver {

    private final TypeResolverSupport typeResolverSupport;

    @Nullable
    @Override
    public final RecursionKey resolve(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType) {
        if (typeResolverSupport.supports(initialSchema)) {
            return resolveInternal(initialSchema, javaType, annotationsSupplier, schemaBuilderFromType);
        }
        return null;
    }

    @Nullable
    protected abstract RecursionKey resolveInternal(InitialSchema initialSchema, JavaType javaType, AnnotationsSupplier annotationsSupplier, SchemaBuilderFromType schemaBuilderFromType);

}
