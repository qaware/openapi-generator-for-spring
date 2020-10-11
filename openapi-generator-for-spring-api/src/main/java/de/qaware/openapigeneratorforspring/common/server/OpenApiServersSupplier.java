package de.qaware.openapigeneratorforspring.common.server;

import de.qaware.openapigeneratorforspring.model.server.Server;

import java.util.List;
import java.util.function.Supplier;

public interface OpenApiServersSupplier extends Supplier<List<Server>> {
}
