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

package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.model.security.SecurityRequirement;

import java.util.Arrays;

public class DefaultSecurityRequirementAnnotationMapper implements SecurityRequirementAnnotationMapper {
    @Override
    public SecurityRequirement mapArray(io.swagger.v3.oas.annotations.security.SecurityRequirement... annotations) {
        return OpenApiMapUtils.buildStringMapFromStream(
                Arrays.stream(annotations),
                io.swagger.v3.oas.annotations.security.SecurityRequirement::name,
                annotation -> Arrays.asList(annotation.scopes()),
                SecurityRequirement::new
        );
    }
}
