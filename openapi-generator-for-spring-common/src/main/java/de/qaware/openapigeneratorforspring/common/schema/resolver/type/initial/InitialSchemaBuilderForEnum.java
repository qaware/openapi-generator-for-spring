package de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class InitialSchemaBuilderForEnum implements InitialSchemaBuilder {
    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Resolver resolver) {
        if (javaType.isEnumImplType()) {
            List<Object> enumValues = Arrays.asList(javaType.getRawClass().getEnumConstants());
            return InitialSchema.of(Schema.builder().type("string").enumValues(enumValues).build());
        }
        return null;
    }
}
