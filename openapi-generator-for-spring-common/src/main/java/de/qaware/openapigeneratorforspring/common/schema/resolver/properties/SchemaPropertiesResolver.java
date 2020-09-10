package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.util.Map;

public interface SchemaPropertiesResolver {
    Map<String, AnnotatedMember> findProperties(JavaType javaType);
}
