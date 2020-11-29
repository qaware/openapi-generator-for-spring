/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: API
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

package de.qaware.openapigeneratorforspring.common.operation.response;

import de.qaware.openapigeneratorforspring.common.operation.OperationBuilderContext;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;

/**
 * Customizer for {@link ApiResponses}. Run AFTER the responses have been
 * built from {@link io.swagger.v3.oas.annotations.responses.ApiResponse
 * api response annotations}.
 */
@FunctionalInterface
public interface OperationApiResponsesCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize given responses via reference to {@link ApiResponses api responses map}.
     *
     * @param apiResponses            map of {@link de.qaware.openapigeneratorforspring.model.response.ApiResponse api responses}
     * @param operationBuilderContext context of operation building
     */
    void customize(ApiResponses apiResponses, OperationBuilderContext operationBuilderContext);
}
