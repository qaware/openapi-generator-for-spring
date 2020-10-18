package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

public class SchemaCustomizerForNullable implements SchemaCustomizer {
    @Override
    public void customizeBeforeProperties(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        if (annotationsSupplier.findFirstAnnotation(Nullable.class) != null) {
            schema.setNullable(true);
        }
    }
}
