package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialSchemaFactoryForCollectionLikeType implements InitialSchemaFactory {


    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
        return javaType.isCollectionLikeType() ? InitialSchema.builder().schema(Schema.builder().type("array").build()).build() : null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
