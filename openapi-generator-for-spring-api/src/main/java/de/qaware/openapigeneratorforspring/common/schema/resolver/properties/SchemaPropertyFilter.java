package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
@FunctionalInterface
public interface SchemaPropertyFilter extends Ordered {

    int DEFAULT_ORDER = 0;

    boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType);


    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
