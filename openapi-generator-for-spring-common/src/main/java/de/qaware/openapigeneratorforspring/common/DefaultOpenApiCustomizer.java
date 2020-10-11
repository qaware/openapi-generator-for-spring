package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.mapper.SecuritySchemeAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.mapper.ServerAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.security.OpenApiSecuritySchemesSupplier;
import de.qaware.openapigeneratorforspring.common.server.OpenApiServersSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.Components;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;
import de.qaware.openapigeneratorforspring.model.server.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultOpenApiCustomizer implements OpenApiCustomizer {
    public static int ORDER = DEFAULT_ORDER;

    private final OpenApiInfoSupplier openApiInfoSupplier;
    private final ServerAnnotationMapper serverAnnotationMapper;
    private final List<OpenApiServersSupplier> openApiServersSuppliers;
    private final SecuritySchemeAnnotationMapper securitySchemeAnnotationMapper;
    private final List<OpenApiSecuritySchemesSupplier> openApiSecuritySchemesSuppliers;
    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;

    @Override
    public void customize(OpenApi openApi) {

        openApi.setInfo(openApiInfoSupplier.get()); // always set info to comply with spec
        // TODO set more properties
        Optional<OpenAPIDefinition> openAPIDefinitionAnnotation = springBootApplicationAnnotationsSupplier.findFirstAnnotation(OpenAPIDefinition.class);
        setServers(openAPIDefinitionAnnotation.map(OpenAPIDefinition::servers).orElse(null), openApi::setServers);

        setMapIfNotEmpty(buildSecuritySchemes(),
                securitySchemes -> {
                    if (openApi.getComponents() == null) {
                        openApi.setComponents(new Components());
                    }
                    openApi.getComponents().setSecuritySchemes(securitySchemes);
                }
        );
    }

    private Map<String, SecurityScheme> buildSecuritySchemes() {
        return Stream.concat(
                buildStringMapFromStream(findSecuritySchemeAnnotations(),
                        io.swagger.v3.oas.annotations.security.SecurityScheme::name,
                        securitySchemeAnnotationMapper::map
                ).entrySet().stream(),
                openApiSecuritySchemesSuppliers.stream()
                        .map(Supplier::get)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    private Stream<io.swagger.v3.oas.annotations.security.SecurityScheme> findSecuritySchemeAnnotations() {
        return Stream.concat(
                springBootApplicationAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecurityScheme.class),
                springBootApplicationAnnotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.security.SecuritySchemes.class).flatMap(x -> Stream.of(x.value()))
        );
    }

    public void setServers(@Nullable io.swagger.v3.oas.annotations.servers.Server[] serverAnnotations, Consumer<List<Server>> setter) {
        List<Server> servers = openApiServersSuppliers.stream()
                .map(Supplier::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (serverAnnotations != null) {
            servers.addAll(serverAnnotationMapper.mapArray(serverAnnotations));
        }
        setCollectionIfNotEmpty(servers, setter);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
