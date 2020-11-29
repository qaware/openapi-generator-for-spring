package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.annotation.Nullable;

public class SchemaCustomizerForNullable implements SchemaCustomizer {
    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        // TODO support more @Nullable annotations?
        annotationsSupplier.findAnnotations(Nullable.class)
                .findFirst().ifPresent(ignored -> schema.setNullable(true));
    }
}
