package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.server.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenAPIDefinitionAnnotationSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
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

@RequiredArgsConstructor
public class DefaultOpenApiCustomizer implements OpenApiCustomizer {
    public static int ORDER = DEFAULT_ORDER;

    private final OpenApiInfoSupplier openApiInfoSupplier;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final List<OpenApiServersSupplier> openApiServersSuppliers;
    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;
    private final OpenAPIDefinitionAnnotationSupplier openAPIDefinitionAnnotationSupplier;

    @Override
    public void customize(OpenApi openApi) {
        openApi.setInfo(openApiInfoSupplier.get()); // always set info to comply with spec
        // TODO set more properties?
        setServers(openApi::setServers);
    }

    public void setServers(Consumer<List<Server>> setter) {
        List<Server> servers = Stream.concat(
                openApiServersSuppliers.stream()
                        .map(Supplier::get)
                        .flatMap(Collection::stream),
                Stream.concat(
                        openAPIDefinitionAnnotationSupplier.getValues(OpenAPIDefinition::servers),
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
