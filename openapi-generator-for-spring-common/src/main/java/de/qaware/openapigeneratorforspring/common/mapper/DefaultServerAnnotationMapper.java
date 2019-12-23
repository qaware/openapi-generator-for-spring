package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultServerAnnotationMapper implements ServerAnnotationMapper {

    private final ServerVariableAnnotationMapper serverVariableAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public List<Server> mapArray(io.swagger.v3.oas.annotations.servers.Server[] serversAnnotations) {
        return Stream.of(serversAnnotations)
                .map(this::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Server> map(io.swagger.v3.oas.annotations.servers.Server serverAnnotation) {
        Server server = new Server();
        AtomicBoolean notEmpty = new AtomicBoolean();
        OpenApiStringUtils.setStringIfNotBlank(serverAnnotation.description(), description -> {
            server.setDescription(description);
            notEmpty.set(true);
        });
        OpenApiStringUtils.setStringIfNotBlank(serverAnnotation.url(), url -> {
            server.setUrl(url);
            notEmpty.set(true);
        });
        if (!notEmpty.get()) {
            return Optional.empty();
        }
        setMapIfNotEmpty(server::setVariables, serverVariableAnnotationMapper.mapArray(serverAnnotation.variables()));
        setMapIfNotEmpty(server::setExtensions, extensionAnnotationMapper.mapArray(serverAnnotation.extensions()));
        return Optional.of(server);
    }
}
