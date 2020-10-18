package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;
import java.util.function.Consumer;

@FunctionalInterface
public interface SchemaCustomizer {
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier,
                   Map<String, SchemaProperty> properties);

    @FunctionalInterface
    interface SchemaProperty {
        void customize(Consumer<Schema> propertySchemaCustomizer);
    }
}
