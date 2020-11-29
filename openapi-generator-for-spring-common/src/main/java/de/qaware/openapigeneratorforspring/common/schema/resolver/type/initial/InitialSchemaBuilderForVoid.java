package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

public class InitialSchemaBuilderForVoid implements InitialSchemaBuilder {
    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        if (Void.TYPE.equals(javaType.getRawClass()) || Void.class.equals(javaType.getRawClass())) {
            return InitialSchema.of(new Schema());
        }
        return null;
    }
}
