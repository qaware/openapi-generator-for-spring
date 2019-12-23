package de.qaware.openapigeneratorforspring.common.mapper;

import java.util.Map;

public interface ExtensionAnnotationMapper {
    Map<String, Object> mapArray(io.swagger.v3.oas.annotations.extensions.Extension[] extensionAnnotations);
}
