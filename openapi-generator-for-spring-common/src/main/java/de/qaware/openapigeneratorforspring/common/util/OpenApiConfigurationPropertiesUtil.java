package de.qaware.openapigeneratorforspring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.function.Predicate;

@NoArgsConstructor
public class OpenApiConfigurationPropertiesUtil {

    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static abstract class ConfigurationPropertyCondition<T> extends SpringBootCondition {
        private final Class<T> propertiesClass;
        private final Predicate<T> predicate;

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return new ConditionOutcome(predicate.test(bindProperties(context)), (String) null);
        }

        private T bindProperties(ConditionContext context) {
            return Binder.get(context.getEnvironment())
                    .bind(ConfigurationPropertyName.of(findPrefixFromClass()), Bindable.of(propertiesClass))
                    .orElseGet(() -> {
                        try {
                            return propertiesClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new IllegalStateException("Cannot create instance of " + propertiesClass);
                        }
                    });
        }

        private String findPrefixFromClass() {
            ConfigurationProperties annotation = AnnotationUtils.findAnnotation(propertiesClass, ConfigurationProperties.class);
            if (annotation == null) {
                throw new IllegalStateException("Cannot find @ConfigurationProperties on " + propertiesClass);
            }
            return annotation.prefix();
        }
    }
}
