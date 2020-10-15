package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupport;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.tags.TagsSupport;
import de.qaware.openapigeneratorforspring.common.tags.TagsSupportFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationAnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class OpenApiGenerator {


    private final PathsBuilder pathsBuilder;
    private final ReferencedItemSupportFactory referencedItemSupportFactory;
    private final TagsSupportFactory tagsSupportFactory;
    private final List<OpenApiCustomizer> openApiCustomizers;
    private final OpenApiSpringBootApplicationAnnotationsSupplier springBootApplicationAnnotationsSupplier;

    public OpenApi generateOpenApi() {

        ReferencedItemSupport referencedItemSupport = referencedItemSupportFactory.create();
        TagsSupport tagsSupport = tagsSupportFactory.create(
                springBootApplicationAnnotationsSupplier.findFirstAnnotation(OpenAPIDefinition.class)
                        .map(OpenAPIDefinition::tags)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty)
        );
        Paths paths = pathsBuilder.buildPaths(
                referencedItemSupport.getReferencedItemConsumerSupplier(),
                tagsSupport.getTagsConsumer()
        );

        OpenApi openApi = new OpenApi();
        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        referencedItemSupport.applyToOpenApi(openApi);
        openApi.setTags(tagsSupport.buildTags());

        openApiCustomizers.forEach(customizer -> customizer.customize(openApi));
        return openApi;
    }
}
