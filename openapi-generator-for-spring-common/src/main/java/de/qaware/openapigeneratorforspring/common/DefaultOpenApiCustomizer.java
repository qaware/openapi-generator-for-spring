package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.server.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.server.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOpenApiCustomizer implements OpenApiCustomizer {
    public static int ORDER = DEFAULT_ORDER;

    private final OpenApiInfoSupplier openApiInfoSupplier;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final List<OpenApiServersSupplier> openApiServersSuppliers;

    @Override
    public void customize(OpenApi openApi, @Nullable OpenAPIDefinition openAPIDefinitionAnnotation) {
        openApi.setInfo(openApiInfoSupplier.get(openAPIDefinitionAnnotation)); // always set info to comply with spec
        // TODO set more properties
        setServers(openAPIDefinitionAnnotation, openApi::setServers);
    }

    public void setServers(@Nullable OpenAPIDefinition openAPIDefinitionAnnotation, Consumer<List<Server>> setter) {
        List<Server> servers = openApiServersSuppliers.stream()
                .map(Supplier::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (openAPIDefinitionAnnotation != null) {
            servers.addAll(serverAnnotationMapper.mapArray(openAPIDefinitionAnnotation.servers()));
        }
        setCollectionIfNotEmpty(servers, setter);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
