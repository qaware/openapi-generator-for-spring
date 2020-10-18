package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;
import java.util.Map;

public class SchemaCustomizerForNullable implements SchemaCustomizer {
    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaProperty> properties) {
        if (annotationsSupplier.findFirstAnnotation(Nullable.class) != null) {
            schema.setNullable(true);
        }
    }
}
