package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.schema.Schema;

public interface SchemaCustomizer {
    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier);
}
