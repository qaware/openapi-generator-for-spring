package de.qaware.openapigeneratorforspring.ui.webjar;

import org.springframework.core.io.Resource;

public interface WebJarResourceTransformer {
    boolean matches(Resource resource);

    String transform(String resourceContent);
}
