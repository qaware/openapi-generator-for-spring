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

import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.DefaultReferenceIdentifierBuilderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceDeciderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierBuilderForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferenceIdentifierConflictResolverForSchema;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaHandlerFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class OpenApiGeneratorReferenceSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedSchemaHandlerFactory referencedSchemaHandlerFactory(
            ReferenceDeciderForSchema referenceDecider,
            ReferenceIdentifierBuilderForSchema referenceIdentifierFactory,
            ReferenceIdentifierConflictResolverForSchema referenceIdentifierConflictResolver
    ) {
        return new ReferencedSchemaHandlerFactory(referenceDecider, referenceIdentifierFactory, referenceIdentifierConflictResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierBuilderForSchema defaultReferenceIdentifierFactoryForSchema() {
        return new DefaultReferenceIdentifierBuilderForSchema();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceIdentifierConflictResolverForSchema defaultReferenceIdentifierConflictResolverForSchema(DefaultReferenceIdentifierConflictResolverFactory factory) {
        return factory.create(defImpl -> defImpl::resolveConflict);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceDeciderForSchema defaultReferenceDeciderForSchema() {
        return new DefaultReferenceDeciderForSchema();
    }
}
