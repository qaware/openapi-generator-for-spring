package de.qaware.openapigeneratorforspring.common.paths.method;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpringWebHandlerMethodResponseCodeMapper {
    public String getResponseCode(HandlerMethod handlerMethod) {
        return handlerMethod.getAnnotationsSupplier().findAnnotations(ResponseStatus.class)
                .map(ResponseStatus::code)
                .mapToInt(HttpStatus::value)
                .mapToObj(Integer::toString)
                .findFirst()
                .orElseGet(() -> Integer.toString(HttpStatus.OK.value()));
    }
}
