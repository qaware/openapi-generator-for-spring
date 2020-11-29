/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: Web
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

package de.qaware.openapigeneratorforspring.common.operation.parameter.converter;


import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.ValueConstants;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiStringUtils.setStringIfNotBlank;

public class SpringWebOpenApiParameterBuilder {

    Parameter build(ParameterIn in, String name, boolean requiredFlag) {
        Parameter parameter = Parameter.builder()
                .in(in.toString())
                .required(requiredFlag)
                .build();
        setStringIfNotBlank(name, parameter::setName);
        return parameter;
    }

    Parameter build(ParameterIn in, String name, boolean required, String defaultValue) {
        return build(in, name, getRequiredFlag(required, defaultValue));
    }

    protected boolean getRequiredFlag(boolean required, String defaultValue) {
        if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
            return false;
        }
        return required;
    }
}
