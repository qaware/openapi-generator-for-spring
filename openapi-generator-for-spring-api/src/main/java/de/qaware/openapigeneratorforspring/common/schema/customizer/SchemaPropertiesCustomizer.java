package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;

@FunctionalInterface
public interface SchemaPropertiesCustomizer {
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier,
                   Map<String, ? extends SchemaProperty> properties);

    @FunctionalInterface
    interface SchemaProperty {
        void customize(SchemaPropertyCustomizer schemaPropertyCustomizer);
    }

    @FunctionalInterface
    interface SchemaPropertyCustomizer {
        void customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier);
    }
}
