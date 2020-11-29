/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Common
 * %%
 * Copyright (C) 2020 QAware GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
