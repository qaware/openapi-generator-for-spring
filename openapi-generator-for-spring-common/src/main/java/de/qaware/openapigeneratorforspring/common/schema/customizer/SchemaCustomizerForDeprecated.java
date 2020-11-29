package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

public class SchemaCustomizerForDeprecated implements SchemaCustomizer {

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        annotationsSupplier.findAnnotations(Deprecated.class)
                .findFirst().ifPresent(ignored -> schema.setDeprecated(true));
    }
}
