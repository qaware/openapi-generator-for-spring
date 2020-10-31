package de.qaware.openapigeneratorforspring.common.reference.component.response;

import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultReferenceIdentifierBuilderForApiResponse implements ReferenceIdentifierBuilderForApiResponse {

    @Override
    @Nullable
    public String buildIdentifier(ApiResponse item, @Nullable String suggestedIdentifier, int numberOfSetters) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void buildIdentifiers(ApiResponse item, List<IdentifierSetter> identifierSetters) {
        String mergedIdentifier = identifierSetters.stream()
                .map(IdentifierSetter::getSuggestedValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .distinct()
                .collect(Collectors.joining("_"));
        if (StringUtils.isBlank(mergedIdentifier)) {
            throw new IllegalStateException("Merged identifier is blank for " + item);
        }
        identifierSetters.forEach(identifierSetter -> identifierSetter.setValue(mergedIdentifier));
    }
}
