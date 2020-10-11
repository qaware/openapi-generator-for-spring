package de.qaware.openapigeneratorforspring.common.util;

import java.util.Optional;

@FunctionalInterface
public interface OpenApiSpringBootApplicationClassSupplier {
    Optional<Class<?>> findSpringBootApplicationClass();
}
