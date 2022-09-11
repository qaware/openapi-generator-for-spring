package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import org.springframework.util.MimeType;

import java.util.Set;

public interface ConsumesMimeTypeProvider extends OpenApiOrderedUtils.DefaultOrdered {
    Set<MimeType> findConsumesMimeTypes(HandlerMethod handlerMethod);
}
