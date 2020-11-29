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

package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;


/**
 * Customizer for {@link Parameter operation parameter}.
 *
 * <p>Is run AFTER the parameter has been {@link
 * de.qaware.openapigeneratorforspring.common.operation.parameter.converter.ParameterMethodConverter
 * converted}.
 *
 * <p>Note that there might be also parameters which are not
 * found from the handler method. Those are also customized,
 * but the {@link OperationParameterCustomizerContext} does
 * not supply a handler method parameter in this case.
 */
@FunctionalInterface
public interface OperationParameterCustomizer extends OpenApiOrderedUtils.DefaultOrdered {
    /**
     * Customize parameter by reference.
     *
     * @param parameter parameter
     * @param context   context for customization
     */
    void customize(Parameter parameter, OperationParameterCustomizerContext context);
}
