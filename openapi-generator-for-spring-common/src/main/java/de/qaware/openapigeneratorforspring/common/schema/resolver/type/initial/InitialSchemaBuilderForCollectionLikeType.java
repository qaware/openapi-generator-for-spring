package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolverSupport;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialSchemaBuilderForCollectionLikeType implements InitialSchemaBuilder, TypeResolverSupport {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        return javaType.isCollectionLikeType() ? buildArrayInitialSchema() : null;
    }

    @Override
    public boolean supports(InitialSchema initialSchema) {
        return initialSchema.getSchema() instanceof ArraySchema;
    }

    public InitialSchema buildArrayInitialSchema() {
        return InitialSchema.builder().schema(new ArraySchema()).build();
    }

    @EqualsAndHashCode(callSuper = true)
    static class ArraySchema extends Schema {
        public ArraySchema() {
            setType("array");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
