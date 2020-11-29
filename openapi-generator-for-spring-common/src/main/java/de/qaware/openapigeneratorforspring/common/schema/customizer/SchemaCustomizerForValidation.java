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
