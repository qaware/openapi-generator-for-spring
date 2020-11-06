package de.qaware.openapigeneratorforspring.ui.webjar;

import org.springframework.core.io.Resource;

import java.util.function.Function;

public interface WebJarResourceTransformer extends Function<String, String> {
    boolean matches(Resource resource);
}
