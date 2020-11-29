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

import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceDeciderForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceIdentifierBuilderForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferenceIdentifierConflictResolverForLink;
import de.qaware.openapigeneratorforspring.common.reference.component.link.ReferencedLinksHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceLinkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedLinksHandlerFactory referencedLinksHandlerFactory(
            ReferenceDeciderForLink referenceDecider,
            ReferenceIdentifierBuilderForLink referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForLink referenceIdentifierConflictResolver
    ) {
        return new ReferencedLinksHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForLink defaultReferenceIdentifierFactoryForLink(DefaultReferenceIdentifierBuilderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::buildIdentifier);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForLink defaultReferenceIdentifierConflictResolverForLink(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForLink defaultReferenceDeciderForLink(DefaultReferenceDeciderFactory factory) {
        return factory.create(defaultImpl -> defaultImpl::turnIntoReference);
    }

}
