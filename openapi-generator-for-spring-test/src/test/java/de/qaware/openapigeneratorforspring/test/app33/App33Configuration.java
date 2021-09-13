package de.qaware.openapigeneratorforspring.test.app33;

import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
class App33Configuration {
    @Bean
    public InitialTypeBuilder openApiSchemaTypeSubstitutionForYourType() {
        return (caller, javaType, annotationsSupplier, recursiveBuilder) -> {
            if (javaType.getRawClass().equals(App33Controller.ResourceId.class)) {
                return recursiveBuilder.build(Long.class, annotationsSupplier);
            }
            return null;
        };
    }

    @Bean
    public Converter<String, App33Controller.ResourceId> resourceIdConverter() {
        return new ResourceIdConverter();
    }

    private static class ResourceIdConverter implements Converter<String, App33Controller.ResourceId> {

        @Override
        public App33Controller.ResourceId convert(String source) {
            return new App33Controller.ResourceId(Long.parseLong(source));
        }
    }
}
