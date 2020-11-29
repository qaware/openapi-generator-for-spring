package de.qaware.openapigeneratorforspring.common.reference.component.securityscheme;

import de.qaware.openapigeneratorforspring.common.reference.ReferencedItemConsumer;
import de.qaware.openapigeneratorforspring.model.security.SecurityScheme;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Consumer for to-be-referenced {@link SecurityScheme security
 * schemes}. They are always referenced as there is no
 * other way than specifying them within the Open Api model.
 */
public interface ReferencedSecuritySchemesConsumer extends Consumer<Map<String, SecurityScheme>>, ReferencedItemConsumer {
}
