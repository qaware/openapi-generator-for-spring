/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Autoconfigure
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

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.component.securityscheme.ReferencedSecuritySchemesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenApiGeneratorReferenceSecuritySchemeAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ReferencedSecuritySchemesHandlerFactory referencedSecuritySchemesHandlerFactory(
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            SecuritySchemeAnnotationMapper openApiSecuritySchemesSuppliers,
            List<OpenApiSecuritySchemesSupplier> securitySchemeAnnotationMappers
    ) {
        return new ReferencedSecuritySchemesHandlerFactory(
                springBootApplicationAnnotationsSupplier, openApiSecuritySchemesSuppliers, securitySchemeAnnotationMappers
        );
    }
}
