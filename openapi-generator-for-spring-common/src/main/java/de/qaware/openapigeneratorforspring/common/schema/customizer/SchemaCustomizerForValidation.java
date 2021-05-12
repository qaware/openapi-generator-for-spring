/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.function.Consumer;

public class SchemaCustomizerForValidation implements SchemaCustomizer {

    @Override
    public void customize(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, RecursiveResolver recursiveResolver) {
        apply(annotationsSupplier, Min.class, a -> schema.setMinimum(new BigDecimal(a.value())));
        apply(annotationsSupplier, Max.class, a -> schema.setMaximum(new BigDecimal(a.value())));
        apply(annotationsSupplier, DecimalMin.class, a -> schema.setMinimum(new BigDecimal(a.value())));
        apply(annotationsSupplier, DecimalMax.class, a -> schema.setMaximum(new BigDecimal(a.value())));
        apply(annotationsSupplier, NotEmpty.class, a -> applyNotEmpty(schema, javaType));
        apply(annotationsSupplier, NotBlank.class, a -> schema.setMinLength(1));
        apply(annotationsSupplier, Negative.class, a -> applyNegative(schema));
        apply(annotationsSupplier, NegativeOrZero.class, a -> schema.setMaximum(BigDecimal.ZERO));
        apply(annotationsSupplier, Positive.class, a -> applyPositive(schema));
        apply(annotationsSupplier, PositiveOrZero.class, a -> schema.setMinimum(BigDecimal.ZERO));
        apply(annotationsSupplier, Pattern.class, a -> schema.setPattern(a.regexp()));
        apply(annotationsSupplier, Size.class, a -> applySize(schema, javaType, a));
        // @NotNull annotation is not necessary, JSON Schema 'nullable' default value is false. See: https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#fixed-fields-20
    }

    private static void applySize(Schema schema, JavaType javaType, Size annotation) {
        if (javaType.getRawClass().isAssignableFrom(CharSequence.class)) {
            if (annotation.min() != 0) {
                schema.setMinLength(annotation.min());
            }
            if (annotation.max() != Integer.MAX_VALUE) {
                schema.setMaxLength(annotation.max());
            }
        } else {
            if (annotation.min() != 0) {
                schema.setMinItems(annotation.min());
            }
            if (annotation.max() != Integer.MAX_VALUE) {
                schema.setMaxItems(annotation.max());
            }
        }
    }

    private static void applyPositive(Schema schema) {
        schema.setMinimum(BigDecimal.ZERO);
        schema.setExclusiveMinimum(true);
    }

    private static void applyNegative(Schema schema) {
        schema.setMaximum(BigDecimal.ZERO);
        schema.setExclusiveMaximum(true);
    }

    private static void applyNotEmpty(Schema schema, JavaType javaType) {
        if (CharSequence.class.isAssignableFrom(javaType.getRawClass())) {
            schema.setMinLength(1);
        } else {
            schema.setMinItems(1);
        }
    }

    private static <T extends Annotation> void apply(AnnotationsSupplier annotationsSupplier, Class<T> annotationClass, Consumer<T> consumer) {
        annotationsSupplier.findAnnotations(annotationClass).findFirst().ifPresent(consumer);
    }

}
