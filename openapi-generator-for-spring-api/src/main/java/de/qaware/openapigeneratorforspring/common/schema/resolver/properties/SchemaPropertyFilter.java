package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;

/**
 * Filter for properties, used by default implementation of {@link SchemaPropertiesResolver}.
 */
@FunctionalInterface
public interface SchemaPropertyFilter extends OpenApiOrderedUtils.DefaultOrdered {

    /**
     * Filter given property with Jackson bean description.
     *
     * @param property               property definition
     * @param beanDescriptionForType bean description from Jackson
     * @return true if property shall be used, false otherwise
     */
    boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType);
}
