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

import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceDeciderForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceIdentifierBuilderForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferenceIdentifierConflictResolverForCallback;
import de.qaware.openapigeneratorforspring.common.reference.component.callback.ReferencedCallbacksHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceCallbackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedCallbacksHandlerFactory referencedCallbacksHandlerFactory(
            ReferenceDeciderForCallback referenceDecider,
            ReferenceIdentifierBuilderForCallback referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForCallback referenceIdentifierConflictResolver
    ) {
        return new ReferencedCallbacksHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForCallback defaultReferenceIdentifierFactoryForCallback(DefaultReferenceIdentifierBuilderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::buildIdentifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForCallback defaultReferenceIdentifierConflictResolverForCallback(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForCallback defaultReferenceDeciderForCallback(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
