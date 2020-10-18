package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.core.Ordered;

@SuppressWarnings("squid:S1214") // suppress warning about constant in interface
public interface SchemaPropertyFilter extends Ordered {

    int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 1000;

    boolean accept(BeanPropertyDefinition property, BeanDescription beanDescriptionForType);
}
