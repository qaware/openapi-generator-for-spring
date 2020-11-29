package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

/**
 * Customizer for {@link Schema}. Run AFTER the initial schema is built
 * and annotations are applied, but BEFORE nested properties are resolved.
 *
 * @see SchemaPropertiesCustomizer for customizing properties
 */
@FunctionalInterface
public interface SchemaCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    /**
     * Customize the given schema by reference.
     *
     * @param schema              schema
     * @param javaType            java type this schema was built from (might be Void)
     * @param annotationsSupplier annotations supplier used during schema building
     */
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
