package de.qaware.openapigeneratorforspring.common.supplier;

import de.qaware.openapigeneratorforspring.model.server.Server;

import java.util.List;
import java.util.function.Supplier;

/**
 * Supplier for list of servers for the Open Api model.
 */
public interface OpenApiServersSupplier extends Supplier<List<Server>> {
}
