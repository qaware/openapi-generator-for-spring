package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.oas.models.links.Link;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildMapFromArray;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;
import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

@RequiredArgsConstructor
public class DefaultLinkAnnotationMapper implements LinkAnnotationMapper {

    private final ParsableValueMapper parsableValueMapper;
    private final ExtensionAnnotationMapper extensionAnnotationMapper;
    private final LinkParameterAnnotationMapper linkParameterAnnotationMapper;
    private final ServerAnnotationMapper serverAnnotationMapper;


    @Override
    public Map<String, Link> mapArray(io.swagger.v3.oas.annotations.links.Link[] linkAnnotations) {
        return buildMapFromArray(
                linkAnnotations,
                io.swagger.v3.oas.annotations.links.Link::name,
                this::map
        );
    }

    @Override
    public Link map(io.swagger.v3.oas.annotations.links.Link linkAnnotation) {
        Link link = new Link();
        setStringIfNotBlank(linkAnnotation.operationRef(), link::setOperationRef);
        setStringIfNotBlank(linkAnnotation.operationId(), link::setOperationId);
        setMapIfNotEmpty(link::setParameters,
                linkParameterAnnotationMapper.mapArray(linkAnnotation.parameters()));
        setStringIfNotBlank(linkAnnotation.requestBody(),
                body -> link.setRequestBody(parsableValueMapper.parse(body))
        );
        setStringIfNotBlank(linkAnnotation.description(), link::setDescription);

        setStringIfNotBlank(linkAnnotation.ref(), link::set$ref);

        setMapIfNotEmpty(link::setExtensions,
                extensionAnnotationMapper.mapArray(linkAnnotation.extensions()));

        serverAnnotationMapper.map(linkAnnotation.server()).ifPresent(link::setServer);

        return link;
    }
}
