package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.model.media.Schema;

/**
 * Builder for {@link Schema#getName() schema name}.
 */
public interface SchemaNameBuilder {
    /**
     * Build name from Jackson java type.
     *
     * @param javaType java type
     * @return schema name, can be non-unique but should be descriptive
     */
    String buildFromType(JavaType javaType);
}
