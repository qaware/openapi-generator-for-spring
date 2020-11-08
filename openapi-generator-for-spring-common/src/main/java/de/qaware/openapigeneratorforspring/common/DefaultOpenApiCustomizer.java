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

@RequiredArgsConstructor
public class DefaultOpenApiCustomizer implements OpenApiCustomizer {
    public static final int ORDER = DEFAULT_ORDER;

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
