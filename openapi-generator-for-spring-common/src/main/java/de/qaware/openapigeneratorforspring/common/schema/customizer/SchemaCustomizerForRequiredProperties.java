package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import java.util.Map;

public class SchemaCustomizerForRequiredProperties implements SchemaPropertiesCustomizer {
    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Map<String, ? extends SchemaProperty> properties) {
        properties.forEach((propertyName, customizer) ->
                customizer.customize((propertySchema, propertyJavaType, propertyAnnotationsSupplier) -> {
                    propertyAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                            .findFirst()
                            .map(io.swagger.v3.oas.annotations.media.Schema::required)
                            .filter(flag -> flag)
                            .ifPresent(ignored -> schema.addRequired(propertyName));
                })
        );
    }
}
