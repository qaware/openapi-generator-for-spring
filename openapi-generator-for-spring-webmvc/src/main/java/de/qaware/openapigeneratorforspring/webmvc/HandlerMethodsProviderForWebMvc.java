/*-
 * #%L
 * OpenAPI Generator for Spring Boot :: WebMVC
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

package de.qaware.openapigeneratorforspring.webmvc;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodWithInfo;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethodsProvider;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebHandlerMethodBuilder;
import de.qaware.openapigeneratorforspring.common.paths.SpringWebRequestMethodEnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HandlerMethodsProviderForWebMvc implements HandlerMethodsProvider {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SpringWebHandlerMethodBuilder springWebHandlerMethodBuilder;
    private final SpringWebRequestMethodEnumMapper springWebRequestMethodEnumMapper;

    @Override
    public List<HandlerMethodWithInfo> getHandlerMethods() {
        return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .map(entry -> new HandlerMethodWithInfo(
                        springWebHandlerMethodBuilder.build(entry.getValue()),
                        findPatterns(entry.getKey()),
                        springWebRequestMethodEnumMapper.map(entry.getKey().getMethodsCondition().getMethods())
                ))
                .collect(Collectors.toList());
    }

    private Set<String> findPatterns(RequestMappingInfo requestMappingInfo) {
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
        if (patternsCondition != null) {
            return patternsCondition.getPatterns();
        } else if (pathPatternsCondition != null) {
            return pathPatternsCondition.getPatterns().stream()
                    .map(PathPattern::getPatternString)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
