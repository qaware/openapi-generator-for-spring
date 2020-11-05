package de.qaware.openapigeneratorforspring.ui.webjar;

import java.net.URI;

public interface WebJarResourceTransformerFactory {
    WebJarResourceTransformer create(URI baseUri);
}
