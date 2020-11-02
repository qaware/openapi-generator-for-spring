package de.qaware.openapigeneratorforspring.common.supplier;

import lombok.Setter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DefaultOpenApiSpringBootApplicationClassSupplier implements OpenApiSpringBootApplicationClassSupplier, ApplicationContextAware {
    @Setter
    @Nullable
    private ApplicationContext applicationContext;

    @Override
    public Optional<Class<?>> findSpringBootApplicationClass() {
        return Optional.ofNullable(applicationContext)
                .map(context -> applicationContext.getBeansWithAnnotation(SpringBootApplication.class))
                .map(Map::values)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one SpringBootApplication annotated bean");
                })
                .map(Object::getClass)
                // remove Spring proxy classes
                .map(ClassUtils::getUserClass);
    }
}
