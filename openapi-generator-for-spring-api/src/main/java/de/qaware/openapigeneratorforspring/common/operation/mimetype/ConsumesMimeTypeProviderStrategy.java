package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import org.springframework.util.MimeType;

import java.util.Set;

public interface ConsumesMimeTypeProviderStrategy {
    Set<MimeType> getConsumesMimeTypes(HandlerMethod handlerMethod);
}
