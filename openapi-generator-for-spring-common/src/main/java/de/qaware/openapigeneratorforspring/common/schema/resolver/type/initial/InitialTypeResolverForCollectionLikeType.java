package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class InitialTypeResolverForCollectionLikeType implements InitialTypeResolver {


    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType) {
        return javaType.isCollectionLikeType() ? InitialSchema.builder().schema(Schema.builder().type("array").build()).build() : null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
