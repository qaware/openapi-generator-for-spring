package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public interface SchemaPropertyFilter {
    boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType);
}
