package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Map;
import java.util.function.Consumer;

public interface ReferencedSecuritySchemesConsumer extends Consumer<Map<String, SecurityScheme>>, ReferencedItemConsumer {
}
