package de.qaware.openapigeneratorforspring.common.paths.method.merger;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringWebHandlerMethodIdentifierMerger {
    public String mergeIdentifiers(List<HandlerMethod> handlerMethods) {
        LinkedList<String> identifiers = handlerMethods.stream()
                .map(HandlerMethod::getIdentifier)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        String commonPrefix = StringUtils.getCommonPrefix(identifiers.getFirst(), identifiers.getLast());
        return Stream.concat(
                Stream.of(commonPrefix),
                identifiers.stream()
                        .map(identifier -> identifier.substring(commonPrefix.length()))
                        .filter(s -> !s.isEmpty())
        ).collect(Collectors.joining());
    }
}
