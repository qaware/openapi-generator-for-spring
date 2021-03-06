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

package de.qaware.openapigeneratorforspring.common.operation.parameter.customizer;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefaultOperationParameterNameCustomizer implements OperationParameterCustomizer {
    @Override
    public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
        context.getHandlerMethodParameter()
                .flatMap(HandlerMethod.Parameter::getName)
                .ifPresent(methodParameterName -> {
                    if (parameter.getName() == null) {
                        parameter.setName(methodParameterName);
                    }
                });
        if (StringUtils.isBlank(parameter.getName())) {
            LOGGER.warn("{} in {} has blank name. This is not allowed by spec.", parameter, context.getOperationInfo());
        }
    }
}
