package de.qaware.openapigeneratorforspring.common.operation.mimetype;

import de.qaware.openapigeneratorforspring.common.paths.HandlerMethod;
import de.qaware.openapigeneratorforspring.common.util.OpenApiOrderedUtils;
import org.springframework.util.MimeType;

import java.util.Set;

public interface ProducesMimeTypeProvider extends OpenApiOrderedUtils.DefaultOrdered {
    Set<MimeType> findProducesMimeTypes(HandlerMethod handlerMethod);
}
