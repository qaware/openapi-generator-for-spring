package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.earlierThan;

public class SchemaPropertyFilterForWeirdMembers implements SchemaPropertyFilter {
    // Run a little bit earlier to get rid of weird properties early
    public static final int ORDER = earlierThan(DEFAULT_ORDER);

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
