package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupport;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.tags.TagsSupport;
import de.qaware.openapigeneratorforspring.common.tags.TagsSupportFactory;
import de.qaware.openapigeneratorforspring.common.util.OpenApiSpringBootApplicationClassSupplier;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiObjectUtils.setIfNotEmpty;

@RequiredArgsConstructor
public class OpenApiGenerator {


    private final PathsBuilder pathsBuilder;
    private final ReferencedItemSupportFactory referencedItemSupportFactory;
    private final TagsSupportFactory tagsSupportFactory;
    private final List<OpenApiCustomizer> openApiCustomizers;
    private final OpenApiSpringBootApplicationClassSupplier springBootApplicationClassSupplier;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;

    public OpenApi generateOpenApi() {
        Optional<OpenAPIDefinition> openAPIDefinitionAnnotation = springBootApplicationClassSupplier.findSpringBootApplicationClass()
                .flatMap(clazz -> annotationsSupplierFactory.createFromAnnotatedElement(clazz).findAnnotations(OpenAPIDefinition.class).findFirst());

        ReferencedItemSupport referencedItemSupport = referencedItemSupportFactory.create();
        TagsSupport tagsSupport = tagsSupportFactory.create(openAPIDefinitionAnnotation
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
        setIfNotEmpty(referencedItemSupport.buildComponents(), openApi::setComponents);
        openApi.setTags(tagsSupport.buildTags());

        openApiCustomizers.forEach(customizer -> customizer.customize(openApi, openAPIDefinitionAnnotation.orElse(null)));
        return openApi;
    }
}
