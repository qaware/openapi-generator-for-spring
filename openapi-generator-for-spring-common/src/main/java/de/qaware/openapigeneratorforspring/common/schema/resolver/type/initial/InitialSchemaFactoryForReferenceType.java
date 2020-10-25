package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

import javax.annotation.Nullable;

public class InitialSchemaFactoryForReferenceType implements InitialSchemaFactory {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema resolveFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialTypeResolver fallbackResolver) {
        if (javaType.isReferenceType()) {
            // TODO append annotationSupplier with contained generic type!
            InitialSchema initialSchema = fallbackResolver.resolveFromType(javaType.getContentType(), annotationsSupplier);
            if (initialSchema != null && initialSchema.getSchema().getNullable() == null) {
                // TODO check if all jackson reference types should be considered @Nullable by default
                initialSchema.getSchema().setNullable(true);
            }
            return initialSchema;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
