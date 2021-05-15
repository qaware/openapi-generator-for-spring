package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;

/**
 * A resolved schema property.
 *
 * @see SchemaPropertiesResolver
 */
public interface SchemaProperty {
    /**
     * Type of the property.
     *
     * @return Jackson java type
     */
    JavaType getType();

    /**
     * Annotations associated with the type.
     *
     * @return annotations supplier
     */
    AnnotationsSupplier getAnnotationsSupplier();
}
