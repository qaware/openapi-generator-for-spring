package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialSchemaBuilderForCollectionLikeType implements InitialSchemaBuilder {


    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver fallbackResolver) {
        return javaType.isCollectionLikeType() ? InitialSchema.builder().schema(Schema.builder().type("array").build()).build() : null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
