package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * Customizer for {@link Schema} properties. As properties
 * are built recursively, this interface is not a simple
 * call to each property. This allows to modify the parent
 * schema, containing the properties, to be modified as well.
 *
 * <p>Prefer using {@link SchemaCustomizer} whenever possible
 */
@FunctionalInterface
public interface SchemaPropertiesCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    /**
     * Customize the given properties (map from name to customizer callback)
     *
     * @param schema              schema owning the given properties
     * @param javaType            java type of owning schema
     * @param annotationsSupplier annotations supplier of owning schema
     * @param properties          map of property name to property customizer callback
     */
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier,
                   Map<String, ? extends SchemaProperty> properties);

    /**
     * Customizer callback for property. Helper interface to provide
     * properties map to {@link SchemaPropertiesCustomizer#customize}.
     */
    @FunctionalInterface
    interface SchemaProperty {
        /**
         * Customizer callback which is called once the property schema is eventually built.
         *
         * @param schemaPropertyCustomizer customizer for schema of property
         */
        void customize(SchemaPropertyCustomizer schemaPropertyCustomizer);
    }

    /**
     * Customizer for schema property.
     */
    @FunctionalInterface
    interface SchemaPropertyCustomizer {
        /**
         * Customize schema of property.
         *
         * @param propertySchema      schema of property
         * @param javaType            java type of property (never Void)
         * @param annotationsSupplier annotations supplier for property (member)
         */
        void customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
