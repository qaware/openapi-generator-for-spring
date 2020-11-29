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

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ExtensionAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ExternalDocumentationAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.supplier.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.server.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.earlierThan;

@RequiredArgsConstructor
public class DefaultOpenApiCustomizer implements OpenApiCustomizer {
    // run somewhat earlier such that other customizers run after this one usually
    public static final int ORDER = earlierThan(DEFAULT_ORDER);

    private final ServerAnnotationMapper serverAnnotationMapper;
    private final ExternalDocumentationAnnotationMapper externalDocumentationAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    private final OpenApiInfoSupplier openApiInfoSupplier;
    private final List<OpenApiServersSupplier> openApiServersSuppliers;

    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;
    private final OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier;

    @Override
    public void customize(OpenApi openApi) {
        openApi.setInfo(openApiInfoSupplier.get()); // always set info to comply with spec
        setServers(openApi::setServers);
        openAPIDefinitionAnnotationSupplier.get().ifPresent(openAPIDefinition -> {
            setIfNotEmpty(externalDocumentationAnnotationMapper.map(openAPIDefinition.externalDocs()), openApi::setExternalDocs);
            setMapIfNotEmpty(extensionAnnotationMapper.mapArray(openAPIDefinition.extensions()), openApi::setExtensions);
        });
    }

    public void setServers(Consumer<List<Server>> setter) {
        List<Server> servers = Stream.concat(
                openApiServersSuppliers.stream()
                        .map(Supplier::get)
                        .flatMap(Collection::stream),
                Stream.concat(
                        openAPIDefinitionAnnotationSupplier.getAnnotations(OpenAPIDefinition::servers),
                        springBootApplicationAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.servers.Server.class)
                ).map(serverAnnotationMapper::map)
        ).collect(Collectors.toList());
        setCollectionIfNotEmpty(servers, setter);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
