package de.qaware.openapigeneratorforspring.common;

import de.qaware.openapigeneratorforspring.common.info.OpenApiInfoSupplier;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.DefaultReferencedApiResponseStorage;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.DefaultReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferenceNameConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponseStorage;
import de.qaware.openapigeneratorforspring.common.operation.response.reference.ReferencedApiResponsesConsumer;
import de.qaware.openapigeneratorforspring.common.paths.PathsBuilder;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.DefaultReferencedSchemaStorage;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferenceNameConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.reference.ReferencedSchemaStorage;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.setMapIfNotEmpty;

@RequiredArgsConstructor
public class OpenApiGenerator {


    private final PathsBuilder pathsBuilder;
    private final OpenApiInfoSupplier openApiInfoSupplier;

    private final ReferenceNameFactory referenceNameFactory;

    private final ReferenceNameConflictResolverForSchema referenceNameConflictResolverForSchema;
    private final ReferenceDeciderForSchema referenceDeciderForSchema;

    private final ReferenceNameConflictResolverForApiResponse referenceNameConflictResolverForApiResponse;
    private final ReferenceDeciderForApiResponse referenceDeciderForApiResponse;


    public OpenAPI generateOpenApi() {

        // TODO introduce factory for his?
        ReferencedSchemaStorage referencedSchemaStorage = new DefaultReferencedSchemaStorage(referenceNameFactory, referenceNameConflictResolverForSchema, referenceDeciderForSchema);
        ReferencedSchemaConsumer referencedSchemaConsumer = new DefaultReferencedSchemaConsumer(referencedSchemaStorage);

        ReferencedApiResponseStorage referencedApiResponseStorage = new DefaultReferencedApiResponseStorage(referenceNameFactory, referenceNameConflictResolverForApiResponse, referenceDeciderForApiResponse);
        ReferencedApiResponsesConsumer referencedApiResponsesConsumer = new DefaultReferencedApiResponsesConsumer(referencedApiResponseStorage);

        Paths paths = pathsBuilder.buildPaths(referencedSchemaConsumer, referencedApiResponsesConsumer);

        OpenAPI openApi = new OpenAPI();

        openApi.setPaths(paths); // always set paths, even if empty to comply with spec
        openApi.setInfo(openApiInfoSupplier.get()); // always set info to comply with spec

        // TODO generalize to other reference components
        Map<String, Schema> referencedSchemas = referencedSchemaStorage.buildReferencedSchemas();
        Map<String, ApiResponse> referencedApiResponses = referencedApiResponseStorage.buildReferencedApiResponses();

        Components components = new Components();
        setMapIfNotEmpty(referencedSchemas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), components::setSchemas);
        setMapIfNotEmpty(referencedApiResponses, components::setResponses);
        if (!components.equals(new Components())) {
            openApi.setComponents(components);
        }


        return openApi;
    }


}
