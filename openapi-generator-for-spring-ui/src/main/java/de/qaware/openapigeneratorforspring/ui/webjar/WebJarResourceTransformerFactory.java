package de.qaware.openapigeneratorforspring.ui.webjar;

import org.springframework.web.util.UriComponents;

public interface WebJarResourceTransformerFactory {
    WebJarResourceTransformer create(UriComponents baseUri);
}
