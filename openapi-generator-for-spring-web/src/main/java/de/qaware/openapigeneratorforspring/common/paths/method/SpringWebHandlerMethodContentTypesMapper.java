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

package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static org.springframework.http.MediaType.ALL_VALUE;

public class SpringWebHandlerMethodContentTypesMapper {
    public static final Set<String> SINGLE_ALL_VALUE = singleton(ALL_VALUE);

    public Set<String> findConsumesContentTypes(SpringWebHandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::consumes);
    }

    public Set<String> findProducesContentTypes(SpringWebHandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::produces);
    }

    private static Set<String> fromRequestMappingAnnotation(HandlerMethod handlerMethod, Function<RequestMapping, String[]> annotationMapper) {
        return handlerMethod.findAnnotations(RequestMapping.class)
                .map(annotationMapper)
                .filter(contentTypes -> !StringUtils.isAllBlank(contentTypes))
                // Spring doc says the first one should win,
                // ie. annotation on class level is overridden by method level
                .findFirst()
                .map(Stream::of)
                .orElse(Stream.of(ALL_VALUE))
                .flatMap(Stream::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
