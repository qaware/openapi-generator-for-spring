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

import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIf;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultSecuritySchemeAnnotationMapper implements SecuritySchemeAnnotationMapper {

    private final OAuthFlowsAnnotationMapper oAuthFlowsAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public SecurityScheme map(io.swagger.v3.oas.annotations.security.SecurityScheme annotation) {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(annotation.type().toString());
        setStringIfNotBlank(annotation.description(), securityScheme::setDescription);
        setStringIfNotBlank(annotation.paramName(), securityScheme::setName); // name from annotation is used for components map!
        setIf(annotation.in(), in -> in != SecuritySchemeIn.DEFAULT, in -> securityScheme.setIn(in.toString()));
        setStringIfNotBlank(annotation.scheme(), securityScheme::setScheme);
        setStringIfNotBlank(annotation.bearerFormat(), securityScheme::setBearerFormat);
        setIfNotEmpty(oAuthFlowsAnnotationMapper.map(annotation.flows()), securityScheme::setFlows);
        setStringIfNotBlank(annotation.openIdConnectUrl(), securityScheme::setOpenIdConnectUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), securityScheme::setExtensions);
        return securityScheme;
    }
}
