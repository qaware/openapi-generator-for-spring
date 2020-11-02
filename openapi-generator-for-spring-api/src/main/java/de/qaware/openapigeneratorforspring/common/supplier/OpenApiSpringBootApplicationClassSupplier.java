package de.qaware.openapigeneratorforspring.common.supplier;

import java.util.Optional;

@FunctionalInterface
public interface OpenApiSpringBootApplicationClassSupplier {
    Optional<Class<?>> findSpringBootApplicationClass();
}
