package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupport;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OpenApiGenerator {


    private final PathsBuilder pathsBuilder;
    private final OpenApiInfoSupplier openApiInfoSupplier;
    private final ReferencedItemSupportFactory referencedItemSupportFactory;
    private final List<OpenApiCustomizer> openApiCustomizers;


    public OpenAPI generateOpenApi() {
        ReferencedItemSupport referencedItemSupport = referencedItemSupportFactory.create();
        Paths paths = pathsBuilder.buildPaths(referencedItemSupport.getReferencedItemConsumerSupplier());
        OpenAPI openApi = new OpenAPI();
        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        openApi.setInfo(openApiInfoSupplier.get()); // always set info to comply with spec
        openApi.setComponents(referencedItemSupport.buildComponents());
        openApiCustomizers.forEach(customizer -> customizer.customize(openApi));
        return openApi;
    }
}
