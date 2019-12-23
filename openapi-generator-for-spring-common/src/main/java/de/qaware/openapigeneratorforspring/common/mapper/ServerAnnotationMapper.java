package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.servers.Server;

import java.util.List;
import java.util.Optional;

public interface ServerAnnotationMapper {
    List<Server> mapArray(io.swagger.v3.oas.annotations.servers.Server[] serversAnnotations);

    Optional<Server> map(io.swagger.v3.oas.annotations.servers.Server serverAnnotation);
}
