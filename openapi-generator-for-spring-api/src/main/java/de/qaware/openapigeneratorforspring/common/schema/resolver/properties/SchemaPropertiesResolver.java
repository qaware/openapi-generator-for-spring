package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.util.Map;

/**
 * Properties resolver for Jackson type.
 *
 * <p>Prefer using {@link SchemaPropertyFilter} whenever possible.
 */
public interface SchemaPropertiesResolver {
    /**
     * Find properties for given java type.
     *
     * @param javaType Jackson java type
     * @return map of properties (key is property name)
     */
    Map<String, AnnotatedMember> findProperties(JavaType javaType);
}
