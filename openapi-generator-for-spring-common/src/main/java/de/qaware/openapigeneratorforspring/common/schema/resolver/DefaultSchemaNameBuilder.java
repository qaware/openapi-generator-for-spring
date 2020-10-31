package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.util.ClassUtils;

public class DefaultSchemaNameBuilder implements SchemaNameBuilder {
    @Override
    public String buildFromType(JavaType javaType) {
        // works better for nested classes
        return ClassUtils.getShortName(javaType.getRawClass());
    }
}
