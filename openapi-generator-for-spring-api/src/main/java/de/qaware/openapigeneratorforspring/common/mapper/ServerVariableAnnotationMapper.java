package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.model.server.ServerVariable;
import de.qaware.openapigeneratorforspring.model.server.ServerVariables;

public interface ServerVariableAnnotationMapper {
    ServerVariables mapArray(io.swagger.v3.oas.annotations.servers.ServerVariable[] serverVariablesAnnotations);

    ServerVariable map(io.swagger.v3.oas.annotations.servers.ServerVariable serverVariableAnnotation);
}
