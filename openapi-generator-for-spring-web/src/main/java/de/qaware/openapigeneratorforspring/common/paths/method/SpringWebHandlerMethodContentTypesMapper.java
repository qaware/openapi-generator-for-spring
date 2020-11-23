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

public class SpringWebHandlerMethodContentTypesMapper {
    public Set<String> findConsumesContentTypes(HandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::consumes);
    }

    public Set<String> findProducesContentTypes(HandlerMethod handlerMethod) {
        return fromRequestMappingAnnotation(handlerMethod, RequestMapping::produces);
    }

    public static Set<String> ifEmptyUseSingleAllValue(Set<String> contentTypes) {
        return contentTypes.isEmpty() ? singleton(org.springframework.http.MediaType.ALL_VALUE) : contentTypes;
    }

    private static Set<String> fromRequestMappingAnnotation(HandlerMethod handlerMethod, Function<RequestMapping, String[]> mapper) {
        return handlerMethod.findAnnotations(RequestMapping.class)
                .filter(annotation -> !StringUtils.isAllBlank(mapper.apply(annotation)))
                // Spring doc says the first one should win,
                // ie. annotation on class level is overridden by method level
                .findFirst()
                .map(Stream::of).orElseGet(Stream::empty) // Optional.toStream()
                .map(mapper)
                .flatMap(Stream::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
