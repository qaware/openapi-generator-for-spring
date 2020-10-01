package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.server.Server;

import java.util.List;
import java.util.Optional;

public interface ServerAnnotationMapper {
    List<Server> mapArray(io.swagger.v3.oas.annotations.servers.Server[] serversAnnotations);

    Optional<Server> map(io.swagger.v3.oas.annotations.servers.Server serverAnnotation);
}
