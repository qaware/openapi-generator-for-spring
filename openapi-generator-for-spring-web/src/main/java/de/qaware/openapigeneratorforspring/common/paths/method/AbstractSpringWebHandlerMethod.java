package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.model.media.Content;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.trait.HasIsEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractSpringWebHandlerMethod implements HandlerMethod {

    @Getter
    private final AnnotationsSupplier annotationsSupplier;
    @Getter
    private final List<Parameter> parameters;

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    public static class SpringWebType implements HandlerMethod.Type {
        private final java.lang.reflect.Type type;
        private final AnnotationsSupplier annotationsSupplier;
    }

    @RequiredArgsConstructor
    public static class SpringWebParameter implements Parameter {
        @Nullable
        private final String parameterName;
        private final Type parameterType;
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
        private final Set<String> consumesContentTypes;
        private final Optional<Type> type;
        private final boolean required;

        @Override
        public void customize(de.qaware.openapigeneratorforspring.model.requestbody.RequestBody requestBody) {
            if (requestBody.getRequired() == null) {
                requestBody.setRequired(required);
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class SpringWebResponse implements Response {
        private final String responseCode;
        private final Set<String> producesContentTypes;
        private final Optional<Type> type;

        public boolean shouldClearContent(Content content) {
            return producesContentTypes.equals(content.keySet());
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
