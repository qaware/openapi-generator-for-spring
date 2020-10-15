package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupport;
import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.model.OpenApi;
import de.qaware.openapigeneratorforspring.model.path.Paths;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OpenApiGenerator {

    private final PathsBuilder pathsBuilder;
    private final ReferencedItemSupportFactory referencedItemSupportFactory;
    private final List<OpenApiCustomizer> openApiCustomizers;

    public OpenApi generateOpenApi() {

        ReferencedItemSupport referencedItemSupport = referencedItemSupportFactory.create();
        Paths paths = pathsBuilder.buildPaths(referencedItemSupport.getReferencedItemConsumerSupplier());
        OpenApi openApi = new OpenApi();
        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        referencedItemSupport.applyToOpenApi(openApi);
        openApiCustomizers.forEach(customizer -> customizer.customize(openApi));
        return openApi;
    }
}
