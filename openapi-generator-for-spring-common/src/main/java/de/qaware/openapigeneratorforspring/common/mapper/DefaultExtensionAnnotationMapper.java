package de.qaware.openapigeneratorforspring.common.mapper;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.extensions.Extension;

import java.util.Map;

public class DefaultExtensionAnnotationMapper implements ExtensionAnnotationMapper {
    @Override
    public Map<String, Object> mapArray(Extension[] extensionAnnotations) {
        return AnnotationsUtils.getExtensions(extensionAnnotations);
    }
}
