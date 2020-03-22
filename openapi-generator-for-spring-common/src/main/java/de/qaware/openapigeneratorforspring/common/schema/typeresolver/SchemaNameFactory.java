package de.qaware.openapigeneratorforspring.common.schema.typeresolver;

import com.fasterxml.jackson.databind.JavaType;

public interface SchemaNameFactory {
    String createFromType(JavaType javaType);
}
