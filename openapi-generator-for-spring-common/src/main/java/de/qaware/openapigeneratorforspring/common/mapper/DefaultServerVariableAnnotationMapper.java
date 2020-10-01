package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils;
import de.qaware.openapigeneratorforspring.model.server.ServerVariable;
import de.qaware.openapigeneratorforspring.model.server.ServerVariables;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiCollectionUtils.setCollectionIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class DefaultServerVariableAnnotationMapper implements ServerVariableAnnotationMapper {

    private final ExtensionAnnotationMapper extensionAnnotationMapper;

    @Override
    public ServerVariables mapArray(io.swagger.v3.oas.annotations.servers.ServerVariable[] serverVariablesAnnotations) {
        return buildStringMapFromArray(
                serverVariablesAnnotations,
                io.swagger.v3.oas.annotations.servers.ServerVariable::name,
                this::map,
                ServerVariables::new
        );
    }

    @Override
    public ServerVariable map(io.swagger.v3.oas.annotations.servers.ServerVariable serverVariableAnnotation) {
        ServerVariable serverVariable = new ServerVariable();
        OpenApiStringUtils.setStringIfNotBlank(serverVariableAnnotation.defaultValue(), serverVariable::set_default);
        OpenApiStringUtils.setStringIfNotBlank(serverVariableAnnotation.description(), serverVariable::setDescription);
        setCollectionIfNotEmpty(Arrays.asList(serverVariableAnnotation.allowableValues()), serverVariable::set_enum);
        setMapIfNotEmpty(extensionAnnotationMapper.mapArray(serverVariableAnnotation.extensions()), serverVariable::setExtensions);
        return serverVariable;
    }
}
