package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;
import java.util.function.Consumer;

public interface SchemaCustomizer {
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier,
                   Map<String, ? extends SchemaProperty> properties);

    interface SchemaProperty {
        JavaType getType();

        AnnotationsSupplier getAnnotationsSupplier();

        void customize(Consumer<Schema> propertySchemaCustomizer);
    }
}
