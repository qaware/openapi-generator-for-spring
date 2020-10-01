package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;

public interface SchemaNameFactory {
    String createFromType(JavaType javaType);
}
