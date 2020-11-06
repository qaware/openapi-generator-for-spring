package de.qaware.openapigeneratorforspring.ui.webjar;

import org.springframework.core.io.Resource;

@FunctionalInterface
public interface WebJarTransformedResourceBuilder {
    Resource buildResource(Resource originalResource, String transformedContent);
}
