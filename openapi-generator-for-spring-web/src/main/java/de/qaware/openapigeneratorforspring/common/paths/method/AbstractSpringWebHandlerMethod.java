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

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.core.ResolvableType;
import org.springframework.util.MimeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractSpringWebHandlerMethod implements HandlerMethod {

    private final List<SpringWebParameter> parameters;

    @Override
    public List<Parameter> getParameters() {
        return new ArrayList<>(parameters);
    }

    public List<SpringWebParameter> getSpringWebParameters() {
        return parameters;
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    @ToString
    public static class SpringWebType implements HandlerMethod.Type {
        private final ResolvableType type;
        @ToString.Exclude
        private final AnnotationsSupplier annotationsSupplier;
    }

    @RequiredArgsConstructor
    public static class SpringWebParameter implements Parameter {
        @Nullable
        private final String parameterName;
        @Getter
        private final SpringWebType parameterType;
        @Getter
        private final AnnotationsSupplier annotationsSupplier;

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(parameterName);
        }

        @Override
        public Optional<Type> getType() {
            return Optional.of(parameterType);
        }
    }

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class SpringWebRequestBody implements RequestBody {
        private final AnnotationsSupplier annotationsSupplier;
        private final Set<MimeType> consumesMimeTypes;
        private final Optional<Type> type;
        @Nullable
        private final Boolean required;

        @Override
        public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            if (requestBody.getRequired() == null && required != null) {
                requestBody.setRequired(required);
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class SpringWebResponse implements Response {
        private final String responseCode;
        private final Set<MimeType> producesMimeTypes;
        private final Optional<Type> type;

        public boolean shouldClearContent(Content content) {
            return producesMimeTypes.stream().map(MimeType::toString).collect(Collectors.toSet()).equals(content.keySet());
        }

        @Override
        public void customize(ApiResponse apiResponse) {
            Content content = apiResponse.getContent();
            boolean hasEmptyContent = content != null && content.size() == 1 && content.values().stream().allMatch(HasIsEmpty::isEmpty);
            if (hasEmptyContent && shouldClearContent(content)) {
                apiResponse.setContent(null);
            }
        }
    }
}
