package de.qaware.openapigeneratorforspring.common.reference;


import de.qaware.openapigeneratorforspring.common.schema.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DefaultReferenceNameFactory implements ReferenceNameFactory {

    private static final String IDENTIFIER_SEPARATOR = "_";
    private static final String NULLABLE_PREFIX = "nullable";
    private final AtomicInteger schemaCounter = new AtomicInteger();
    private final AtomicInteger apiResponseCounter = new AtomicInteger();
    private final AtomicInteger parameterCounter = new AtomicInteger();

    @Override
    public ReferenceName create(Object object, @Nullable String suggestedIdentifier) {
        // TODO introduce ReferenceNameFactoryForType
        if (object instanceof Schema) {
            Schema schema = (Schema) object;
            return new ReferenceName(ReferenceName.Type.SCHEMA, getIdentifierForSchema(schema, suggestedIdentifier));
        } else if (object instanceof ApiResponse) {
            return new ReferenceName(ReferenceName.Type.API_RESPONSE, getIdentifierForApiResponse(suggestedIdentifier));
        } else if (object instanceof Parameter) {
            return new ReferenceName(ReferenceName.Type.PARAMETER, getIdentifierForParameter(suggestedIdentifier));
        }
        throw new IllegalStateException("Unknown object to create reference name for " + object.getClass());
    }

    private String getIdentifierForParameter(@Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? suggestedIdentifier
                : "Parameter" + IDENTIFIER_SEPARATOR + parameterCounter.getAndIncrement();
    }

    private String getIdentifierForApiResponse(@Nullable String suggestedIdentifier) {
        return StringUtils.isNotBlank(suggestedIdentifier) ? suggestedIdentifier
                : "ApiResponse" + IDENTIFIER_SEPARATOR + apiResponseCounter.getAndIncrement();
    }

    private String getIdentifierForSchema(Schema schema, @Nullable String suggestedIdentifier) {
        List<Object> identifierComponents = new ArrayList<>();
        if (StringUtils.isNotBlank(suggestedIdentifier)) {
            identifierComponents.add(suggestedIdentifier);
        } else if (StringUtils.isNotBlank(schema.getType())) {
            if (Boolean.TRUE.equals(schema.getNullable())) {
                identifierComponents.add(NULLABLE_PREFIX);
            }
            identifierComponents.add(schema.getType());
            if (StringUtils.isNotBlank(schema.getFormat())) {
                identifierComponents.add(schema.getFormat());
            }
        } else {
            identifierComponents.addAll(Arrays.asList("Schema", schemaCounter.getAndIncrement()));
        }
        return identifierComponents.stream()
                .map(Object::toString)
                .collect(Collectors.joining(IDENTIFIER_SEPARATOR));
    }
}
