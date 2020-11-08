package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForReferenceType implements InitialSchemaBuilder {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        if (javaType.isReferenceType()) {
            // TODO append annotationSupplier with contained generic type!
            InitialSchema initialSchema = resolver.resolveFromType(javaType.getContentType(), annotationsSupplier);
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
