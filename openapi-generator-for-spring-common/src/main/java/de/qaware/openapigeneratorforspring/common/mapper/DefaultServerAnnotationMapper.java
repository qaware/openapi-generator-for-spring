package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.server.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultServerAnnotationMapper implements ServerAnnotationMapper {

    private final ServerVariableAnnotationMapper serverVariableAnnotationMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public List<Server> mapArray(io.swagger.v3.oas.annotations.servers.Server[] serversAnnotations) {
        return Stream.of(serversAnnotations)
                .map(this::map)
                .filter(server -> !server.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public Server map(io.swagger.v3.oas.annotations.servers.Server serverAnnotation) {
        Server server = new Server();
        setStringIfNotBlank(serverAnnotation.description(), server::setDescription);
        setStringIfNotBlank(serverAnnotation.url(), server::setUrl);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(serverAnnotation.extensions()), server::setExtensions);
        setMapIfNotEmpty(serverVariableAnnotationMapper.mapArray(serverAnnotation.variables()), server::setVariables);
        return server;
    }
}
