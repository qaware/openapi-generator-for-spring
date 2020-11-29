/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Model
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

package de.qaware.openapigeneratorforspring.model.security;

import de.qaware.openapigeneratorforspring.model.trait.HasExtensions;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.Data;

import java.util.Map;

/**
 * OAuthFlows
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#oauthFlowsObject"
 */
@Data
public class OAuthFlows implements HasExtensions, HasIsEmpty<OAuthFlows> {
    private OAuthFlow implicit;
    private OAuthFlow password;
    private OAuthFlow clientCredentials;
    private OAuthFlow authorizationCode;
    private Map<String, Object> extensions;

    @Override
    public OAuthFlows createInstance() {
        return new OAuthFlows();
    }
}

