package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;

public interface ServerVariableAnnotationMapper {
    ServerVariables mapArray(io.swagger.v3.oas.annotations.servers.ServerVariable[] serverVariablesAnnotations);

    ServerVariable map(io.swagger.v3.oas.annotations.servers.ServerVariable serverVariableAnnotation);
}
