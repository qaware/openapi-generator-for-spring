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

package de.qaware.openapigeneratorforspring.common.operation.customizer;

import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOperationServersCustomizer implements OperationCustomizer {

    public static final int ORDER = DEFAULT_ORDER;

    private final ServerAnnotationMapper serverAnnotationMapper;

    @Override
    public void customize(Operation operation, OperationBuilderContext operationBuilderContext) {
        List<Server> servers = Stream.concat(
                operation.getServers() == null ? Stream.empty() : operation.getServers().stream(),
                collectServersFromHandlerMethod(operationBuilderContext)
        )
                .distinct()
                .collect(Collectors.toList());
        setCollectionIfNotEmpty(servers, operation::setServers);
    }

    private Stream<Server> collectServersFromHandlerMethod(OperationBuilderContext operationBuilderContext) {
        return operationBuilderContext.getOperationInfo().getHandlerMethod()
                .findAnnotations(io.swagger.v3.oas.annotations.servers.Server.class)
                .map(serverAnnotationMapper::map);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
