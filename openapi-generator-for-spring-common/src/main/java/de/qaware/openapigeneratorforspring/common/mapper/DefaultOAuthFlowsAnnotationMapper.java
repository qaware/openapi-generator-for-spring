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

import de.qaware.openapigeneratorforspring.model.security.OAuthFlows;
import lombok.RequiredArgsConstructor;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOAuthFlowsAnnotationMapper implements OAuthFlowsAnnotationMapper {

    private final OAuthFlowAnnotationMapper oAuthFlowAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public OAuthFlows map(io.swagger.v3.oas.annotations.security.OAuthFlows annotation) {
        OAuthFlows oAuthFlows = new OAuthFlows();
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.implicit()), oAuthFlows::setImplicit);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.password()), oAuthFlows::setPassword);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.clientCredentials()), oAuthFlows::setClientCredentials);
        setIfNotEmpty(oAuthFlowAnnotationMapper.map(annotation.authorizationCode()), oAuthFlows::setAuthorizationCode);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(annotation.extensions()), oAuthFlows::setExtensions);
        return oAuthFlows;
    }
}
