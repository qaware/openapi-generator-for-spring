/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebMVC
 * %%
 * Copyright (C) 2020 - 2021 QAware GmbH
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

package de.qaware.openapigeneratorforspring.webmvc;

import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Set;
import java.util.stream.Collectors;

public class HandlerMethodPathPatternsProviderForWebMvc {
    public Set<String> findPatterns(RequestMappingInfo requestMappingInfo) {
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
        if (patternsCondition != null) {
            return patternsCondition.getPatterns();
        } else if (pathPatternsCondition != null) {
            return pathPatternsCondition.getPatterns().stream()
                    .map(PathPattern::getPatternString)
                    .collect(Collectors.toSet());
        }
        throw new IllegalStateException("The given requestMappingInfo instance does neither have patternsCondition nor pathPatternsCondition");
    }
}
