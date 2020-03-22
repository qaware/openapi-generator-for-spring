package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.util.ClassUtils;

public class DefaultSchemaNameFactory implements SchemaNameFactory {
    @Override
    public String createFromType(JavaType javaType) {
        // works better for nested classes
        return ClassUtils.getShortName(javaType.getRawClass());
    }
}
