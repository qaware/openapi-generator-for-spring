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

package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupport;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OpenApiGenerator {

    private final PathsBuilder pathsBuilder;
    private final ReferencedItemSupportFactory referencedItemSupportFactory;
    private final List<OpenApiCustomizer> openApiCustomizers;

    public OpenApi generateOpenApi() {

        ReferencedItemSupport referencedItemSupport = referencedItemSupportFactory.create();
        Paths paths = pathsBuilder.buildPaths(referencedItemSupport.getReferencedItemConsumerSupplier());
        OpenApi openApi = new OpenApi();
        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        referencedItemSupport.applyToOpenApi(openApi);
        openApiCustomizers.forEach(customizer -> customizer.customize(openApi));
        return openApi;
    }
}
