package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class SchemaPropertyFilterForWeirdMembers implements SchemaPropertyFilter {

    public static final int ORDER = DEFAULT_ORDER - 100; // Run a little bit earlier to get rid of weird properties early

    @Override
    public boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType) {
        // safe-guard weird properties
        return Objects.nonNull(property.getAccessor()) && StringUtils.isNotBlank(property.getName());
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
