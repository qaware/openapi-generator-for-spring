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

package de.qaware.openapigeneratorforspring.common.supplier;

import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultOpenApiServersSupplier implements OpenApiServersSupplier {
    private final OpenApiBaseUriSupplier openApiBaseUriSupplier;

    @Override
    public List<Server> get() {
        URI baseUri = openApiBaseUriSupplier.getBaseUri();
        if (StringUtils.isBlank(baseUri.getPath()) || baseUri.getPath().trim().equals("/")) {
            return Collections.emptyList();
        }
        return Collections.singletonList(Server.builder()
                .description("Default Server")
                .url(baseUri.toString())
                .build()
        );
    }
}
