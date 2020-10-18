package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class SchemaPropertyFilterForIgnoredMembers implements SchemaPropertyFilter {

    public static int ORDER = DEFAULT_ORDER;

    @Override
    public boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType) {
        return !beanDescriptionForType.getIgnoredPropertyNames().contains(property.getName());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
