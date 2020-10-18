package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;

public interface SchemaCustomizer {
    default void customizeBeforeProperties(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        // do nothing
    }

    default void customizeAfterProperties(Schema schema, Map<String, SchemaProperty> properties) {
        // do nothing
    }

    interface SchemaProperty {
        JavaType getType();

        AnnotationsSupplier getAnnotationsSupplier();
    }
}
