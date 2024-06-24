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

package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.reference.handler.AbstractReferencedComponentHandler;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemBuildContext;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.ensureKeyIsNotBlank;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@Slf4j
public class ReferencedSecuritySchemesHandlerImpl extends AbstractReferencedComponentHandler implements ReferencedSecuritySchemesConsumer {

    private final Map<String, SecurityScheme> securitySchemes;

    public ReferencedSecuritySchemesHandlerImpl(
            OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier,
            SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper,
            List<OpenApiSecuritySchemesSupplier> openApiSecuritySchemesSuppliers
    ) {
        this.securitySchemes = Stream.concat(
                buildStringMapFromStream(
                        springBootApplicationAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityScheme.class),
                        io.swagger.v3.oas.annotations.security.SecurityScheme::name,
                        securitySchemeAnnotationMapper::map
                ).entrySet().stream(),
                openApiSecuritySchemesSuppliers.stream()
                        .map(Supplier::get)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
        ).collect(Collectors.toMap(ensureKeyIsNotBlank(Map.Entry::getKey), Map.Entry::getValue, (a, b) -> {
            if (!Objects.equals(a, b)) {
                LOGGER.debug("Non-identical security scheme with conflicting name found. Preferring {} over {}", b, a);
            }
            return b;
        }, LinkedHashMap::new));
    }

    @Override
    public void accept(Map<String, SecurityScheme> map) {
        map.forEach((name, securityScheme) -> securitySchemes.merge(name, securityScheme, (a, b) -> {
            if (a.equals(b)) {
                return a;
            }
            throw new IllegalStateException("Conflicting security schemes with same name found: " + a + " vs. " + b);
        }));
    }

    @Override
    public void applyToComponents(Components components, @Nullable ReferencedItemBuildContext context) {
        setMapIfNotEmpty(securitySchemes, components::setSecuritySchemes);
    }
}
