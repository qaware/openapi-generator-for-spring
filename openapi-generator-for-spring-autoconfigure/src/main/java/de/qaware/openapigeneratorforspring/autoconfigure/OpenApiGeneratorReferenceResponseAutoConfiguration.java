/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Autoconfigure
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

package de.qaware.openapigeneratorforspring.autoconfigure;

import de.qaware.openapigeneratorforspring.common.reference.component.response.DefaultReferenceIdentifierBuilderForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceDeciderForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceIdentifierBuilderForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferenceIdentifierConflictResolverForApiResponse;
import de.qaware.openapigeneratorforspring.common.reference.component.response.ReferencedApiResponsesHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedApiResponsesHandlerFactory referencedApiResponsesHandlerFactory(
            ReferenceDeciderForApiResponse referenceDecider,
            ReferenceIdentifierBuilderForApiResponse referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForApiResponse referenceIdentifierConflictResolver
    ) {
        return new ReferencedApiResponsesHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForApiResponse defaultReferenceIdentifierFactoryForApiResponse() {
        return new DefaultReferenceIdentifierBuilderForApiResponse();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForApiResponse defaultReferenceIdentifierConflictResolverForApiResponse(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForApiResponse defaultReferenceDeciderForApiResponse(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }
}
