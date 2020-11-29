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

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemSupportFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceDeciderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierBuilderFactory;
import de.qaware.openapigeneratorforspring.common.reference.fortype.DefaultReferenceIdentifierConflictResolverFactory;
import de.qaware.openapigeneratorforspring.common.reference.handler.ReferencedItemHandlerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({
        OpenApiGeneratorReferenceCallbackAutoConfiguration.class,
        OpenApiGeneratorReferenceExampleAutoConfiguration.class,
        OpenApiGeneratorReferenceHeaderAutoConfiguration.class,
        OpenApiGeneratorReferenceLinkAutoConfiguration.class,
        OpenApiGeneratorReferenceParameterAutoConfiguration.class,
        OpenApiGeneratorReferenceRequestBodyAutoConfiguration.class,
        OpenApiGeneratorReferenceResponseAutoConfiguration.class,
        OpenApiGeneratorReferenceSchemaAutoConfiguration.class,
        OpenApiGeneratorReferenceSecuritySchemeAutoConfiguration.class,
        OpenApiGeneratorReferenceTagAutoConfiguration.class
})
public class OpenApiGeneratorReferenceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReferencedItemSupportFactory referencedItemSupportFactory(List<ReferencedItemHandlerFactory> referencedItemHandlerFactories) {
        return new ReferencedItemSupportFactory(referencedItemHandlerFactories);
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultReferenceIdentifierConflictResolverFactory defaultReferenceIdentifierConflictResolverForTypeFactory() {
        return new DefaultReferenceIdentifierConflictResolverFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultReferenceDeciderFactory defaultReferenceDeciderFactory() {
        return new DefaultReferenceDeciderFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultReferenceIdentifierBuilderFactory defaultReferenceIdentifierBuilderFactory() {
        return new DefaultReferenceIdentifierBuilderFactory();
    }

}
