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

package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils.laterThan;

@RequiredArgsConstructor
public class SpringWebRequestMappingAnnotationMimeTypesProvider implements ConsumesMimeTypeProvider, ProducesMimeTypeProvider {
    // give user-provided providers (if any) precedence by default
    public static final int ORDER = laterThan(DEFAULT_ORDER);

    @Override
    public Set<MimeType> findConsumesMimeTypes(HandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::consumes);
    }

    @Override
    public Set<MimeType> findProducesMimeTypes(HandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::produces);
    }

    private static Set<MimeType> fromRequestMappingAnnotation(HandlerMethod handlerMethod, Function<RequestMapping, String[]> annotationMapper) {
        return handlerMethod.findAnnotations(RequestMapping.class)
                .map(annotationMapper)
                .filter(contentTypes -> !StringUtils.isAllBlank(contentTypes))
                // Spring doc says the first one should win,
                // i.e. annotation on class level is overridden by method level, there is no merging!
                .findFirst()
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(MimeType::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
