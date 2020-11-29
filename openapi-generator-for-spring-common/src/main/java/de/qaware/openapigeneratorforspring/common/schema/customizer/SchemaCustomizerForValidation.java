package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class SchemaCustomizerForValidation implements SchemaCustomizer {
    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
        annotationsSupplier.findAnnotations(Min.class).findFirst()
                .ifPresent(annotation -> schema.setMinimum(new BigDecimal(annotation.value())));
        annotationsSupplier.findAnnotations(Max.class).findFirst()
                .ifPresent(annotation -> schema.setMaximum(new BigDecimal(annotation.value())));
        annotationsSupplier.findAnnotations(DecimalMin.class).findFirst()
                .ifPresent(annotation -> schema.setMinimum(new BigDecimal(annotation.value())));
        annotationsSupplier.findAnnotations(DecimalMax.class).findFirst()
                .ifPresent(annotation -> schema.setMaximum(new BigDecimal(annotation.value())));

        // TODO support more annotations from javax.validation.*
    }
}
