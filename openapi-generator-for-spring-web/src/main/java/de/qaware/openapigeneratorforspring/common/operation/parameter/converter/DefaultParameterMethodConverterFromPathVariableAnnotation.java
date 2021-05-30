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
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;

public class DefaultParameterMethodConverterFromPathVariableAnnotation extends ParameterMethodConverterFromAnnotation<PathVariable> {

    public static final int ORDER = DEFAULT_ORDER;


    public DefaultParameterMethodConverterFromPathVariableAnnotation() {
        super(PathVariable.class);
    }

    @Nullable
    @Override
    protected Parameter buildParameter(PathVariable annotation) {
        return createDefaultParameterBuilder(annotation.name())
                .in(ParameterIn.PATH.toString())
                // the spec says that if the parameter location is "path",
                // it must be required
                .required(true)
                .build();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
