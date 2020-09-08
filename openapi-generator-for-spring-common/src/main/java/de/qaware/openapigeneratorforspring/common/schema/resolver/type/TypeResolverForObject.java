package de.qaware.openapigeneratorforspring.common.schema.resolver.type;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolverForObject;

import javax.annotation.Nullable;

public class TypeResolverForObject extends AbstractTypeResolver {

    public static final int ORDER = DEFAULT_ORDER;

    @Nullable
    @Override
    protected Schema resolveSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        if (javaType.getRawClass().equals(Object.class)) {
            // accept object schema without any properties as fallback here
            return InitialTypeResolverForObject.OBJECT_SCHEMA_SUPPLIER.get();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
