package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface SchemaCustomizer extends Ordered {
    int DEFAULT_ORDER = 0;

    void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier);

    @Override
    default int getOrder() {
        return DEFAULT_ORDER;
    }
}
