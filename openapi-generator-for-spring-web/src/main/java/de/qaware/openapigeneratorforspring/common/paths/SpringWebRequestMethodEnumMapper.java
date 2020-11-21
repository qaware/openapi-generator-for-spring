package de.qaware.openapigeneratorforspring.common.paths;

import de.qaware.openapigeneratorforspring.model.path.RequestMethod;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SpringWebRequestMethodEnumMapper {
    public Set<RequestMethod> map(Collection<org.springframework.web.bind.annotation.RequestMethod> requestMethods) {
        return requestMethods.stream()
                .map(Enum::name)
                .map(RequestMethod::valueOf)
                .collect(Collectors.toSet());
    }
}
