package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.paths.method.SpringWebHandlerMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringWebHandlerMethodIdentifierMerger {
    private static final String SEPARATOR = "_";

    public String mergeIdentifiers(List<SpringWebHandlerMethod> handlerMethods) {
        LinkedList<String> identifiers = handlerMethods.stream()
                .map(HandlerMethod::getIdentifier)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        String commonPrefix = getCommonPrefix(identifiers);
        return Stream.concat(
                Stream.of(commonPrefix),
                identifiers.stream()
                        .map(identifier -> stripCommonPrefix(commonPrefix, identifier))
                        .filter(s -> !s.isEmpty())
        ).collect(Collectors.joining(SEPARATOR));
    }

    public String stripCommonPrefix(String commonPrefix, String identifier) {
        String stripped = identifier.substring(commonPrefix.length());
        if (stripped.startsWith(SEPARATOR)) {
            return stripped.substring(SEPARATOR.length());
        }
        return stripped;
    }

    public String getCommonPrefix(Deque<String> identifiers) {
        String commonPrefix = StringUtils.getCommonPrefix(identifiers.getFirst(), identifiers.getLast());
        if (commonPrefix.endsWith(SEPARATOR)) {
            return commonPrefix.substring(0, commonPrefix.length() - SEPARATOR.length());
        }
        return commonPrefix;
    }
}
