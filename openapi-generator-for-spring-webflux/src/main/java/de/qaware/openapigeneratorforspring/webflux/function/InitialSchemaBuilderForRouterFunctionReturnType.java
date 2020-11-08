package de.qaware.openapigeneratorforspring.webflux.function;

import com.fasterxml.jackson.databind.JavaType;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.SchemaResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchema;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilder;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaBuilderForSchemaAnnotation;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialSchemaTypeResolver;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class InitialSchemaBuilderForRouterFunctionReturnType implements InitialSchemaBuilder {
    public static int ORDER = InitialSchemaBuilderForSchemaAnnotation.ORDER + 10;

    private static final ReferencedSchemaConsumer NOOP_CONSUMER = new ReferencedSchemaConsumer() {
        @Override
        public void alwaysAsReference(Schema schema, Consumer<Schema> setter) {
            // do nothing
        }

        @Override
        public void maybeAsReference(Schema item, Consumer<Schema> setter) {
            // do nothing
        }
    };


    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;

    @Nullable
    @Override
    public InitialSchema buildFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, InitialSchemaTypeResolver resolver) {
        if (javaType.getRawClass().equals(RouterFunctionHandlerMethod.ReturnType.class)) {
            io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = annotationsSupplier.findFirstAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            if (schemaAnnotation != null) {
                AtomicBoolean interaction = new AtomicBoolean();
                Schema schema = schemaAnnotationMapperFactory.create(createNoopSchemaResolver(interaction))
                        .buildFromAnnotation(schemaAnnotation, NOOP_CONSUMER);
                if (!schema.isEmpty() || interaction.get()) {
                    return InitialSchema.of(new Schema());
                }
            }
        }
        return null;
    }

    private static SchemaResolver createNoopSchemaResolver(AtomicBoolean interaction) {
        return new SchemaResolver() {
            @Override
            public void resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter) {
                interaction.set(true);
            }

            @Override
            public Schema resolveFromClassWithoutReference(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer) {
                if (!Void.class.equals(clazz)) {
                    interaction.set(true);
                }
                return new Schema();
            }
        };
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
