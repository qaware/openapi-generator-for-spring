package de.qaware.openapigeneratorforspring.test.app50;

import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.DefaultOperationParameterSchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.operation.parameter.customizer.OperationParameterCustomizerContext;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.lang.reflect.Type;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver.Mode.FOR_DESERIALIZATION;

@Configuration
@RequiredArgsConstructor
public class App50Configuration {

    @Bean
    public DefaultOperationParameterSchemaCustomizer conversionServiceAwareOperationParameterSchemaCustomizer(SchemaResolver schemaResolver, ConversionService conversionService) {
        return new DefaultOperationParameterSchemaCustomizer(schemaResolver) {
            @Override
            public void customize(Parameter parameter, OperationParameterCustomizerContext context) {
                context.getHandlerMethodParameter().ifPresent(handlerMethodParameter ->
                        handlerMethodParameter.getType().ifPresent(parameterType -> {
                            ReferencedSchemaConsumer referencedSchemaConsumer = context.getReferencedItemConsumer(ReferencedSchemaConsumer.class);
                            AnnotationsSupplier annotationsSupplier = handlerMethodParameter.getAnnotationsSupplier()
                                    .andThen(parameterType.getAnnotationsSupplier());
                            schemaResolver.resolveFromType(FOR_DESERIALIZATION, getType(parameterType.getType()), annotationsSupplier, referencedSchemaConsumer, parameter::setSchema);
                        })
                );
            }

            private Type getType(Type parameterType) {
                TypeDescriptor parameterTypeDescriptor = new TypeDescriptor(ResolvableType.forType(parameterType), null, null);
                TypeDescriptor stringType = TypeDescriptor.valueOf(String.class);
                val canConvertFromString = conversionService.canConvert(stringType, parameterTypeDescriptor);
                if (canConvertFromString) {
                    return stringType.getType();
                }
                return parameterType;
            }
        };
    }
}
