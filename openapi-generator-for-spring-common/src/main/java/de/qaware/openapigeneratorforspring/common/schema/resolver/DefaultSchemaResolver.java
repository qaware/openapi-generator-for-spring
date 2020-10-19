package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplierFactory;
import de.qaware.openapigeneratorforspring.common.reference.component.schema.ReferencedSchemaConsumer;
import de.qaware.openapigeneratorforspring.common.schema.customizer.SchemaCustomizer;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapper;
import de.qaware.openapigeneratorforspring.common.schema.mapper.SchemaAnnotationMapperFactory;
import de.qaware.openapigeneratorforspring.common.schema.resolver.properties.SchemaPropertiesResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.TypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver;
import de.qaware.openapigeneratorforspring.common.schema.resolver.type.initial.InitialTypeResolver.InitialSchema;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils.buildStringMapFromStream;

@RequiredArgsConstructor
public class DefaultSchemaResolver implements SchemaResolver {

    private final OpenApiObjectMapperSupplier openApiObjectMapperSupplier;
    private final SchemaPropertiesResolver schemaPropertiesResolver;
    private final SchemaAnnotationMapperFactory schemaAnnotationMapperFactory;
    private final AnnotationsSupplierFactory annotationsSupplierFactory;
    private final List<TypeResolver> typeResolvers;
    private final List<InitialTypeResolver> initialTypeResolvers;
    private final List<SchemaCustomizer> schemaCustomizers;

    @Override
    public Schema resolveFromClassWithoutReference(Class<?> clazz, ReferencedSchemaConsumer referencedSchemaConsumer) {
        return resolveFromTypeWithoutReference(clazz, annotationsSupplierFactory.createFromAnnotatedElement(clazz), referencedSchemaConsumer);
    }

    @Override
    public void resolveFromType(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer, Consumer<Schema> schemaSetter) {
        referencedSchemaConsumer.maybeAsReference(resolveFromTypeWithoutReference(type, annotationsSupplier, referencedSchemaConsumer), schemaSetter);
    }

    private Schema resolveFromTypeWithoutReference(Type type, AnnotationsSupplier annotationsSupplier, ReferencedSchemaConsumer referencedSchemaConsumer) {
        ObjectMapper mapper = openApiObjectMapperSupplier.get();
        Context context = new Context(schemaAnnotationMapperFactory.create(this), referencedSchemaConsumer);
        JavaType javaType = mapper.constructType(type);
        AtomicReference<Schema> schemaHolder = new AtomicReference<>();
        context.buildSchemaFromTypeRecursively(javaType, annotationsSupplier, schemaHolder::set, 0);
        context.resolveReferencedSchemas();
        return schemaHolder.get();
    }

    @RequiredArgsConstructor
    private class Context {

        private final SchemaAnnotationMapper schemaAnnotationMapper;
        private final ReferencedSchemaConsumer referencedSchemaConsumer;
        private final DefaultSchemaResolverSupport defaultSchemaResolverSupport = new DefaultSchemaResolverSupport();

        void buildSchemaFromTypeRecursively(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, int recursionDepth) {
            for (TypeResolver typeResolver : typeResolvers) {
                boolean resolved = typeResolver.resolveFromType(
                        javaType, annotationsSupplier, schemaConsumer,
                        // this allows the TypeResolver to control recursion
                        (type, supplier, consumer) -> buildSchemaFromType(type, supplier, consumer, recursionDepth),
                        (type, supplier, consumer) -> buildSchemaFromTypeRecursively(type, supplier, consumer, recursionDepth + 1)
                );
                if (resolved) {
                    return;
                }
            }
            buildSchemaFromType(javaType, annotationsSupplier, schemaConsumer, recursionDepth);
        }

        private void buildSchemaFromType(JavaType javaType, AnnotationsSupplier annotationsSupplier, Consumer<Schema> schemaConsumer, int recursionDepth) {

            InitialSchema initialSchema = buildSchemaFromTypeWithoutProperties(javaType, annotationsSupplier);
            Schema schemaWithoutProperties = initialSchema.getSchema();
            Map<String, AnnotatedMember> properties = initialSchema.isIgnoreNestedProperties() ? Collections.emptyMap()
                    : schemaPropertiesResolver.findProperties(javaType);

            Map<String, PropertyCustomizer> propertyCustomizers = customizeSchema(schemaWithoutProperties, javaType, annotationsSupplier, properties.keySet());

            defaultSchemaResolverSupport.consumeSchemaWithProperties(schemaWithoutProperties, properties, recursionDepth == 0, schemaConsumer, (propertyName, member) -> {
                AnnotationsSupplier propertyAnnotationsSupplier = annotationsSupplierFactory.createFromMember(member)
                        .andThen(annotationsSupplierFactory.createFromAnnotatedElement(member.getType().getRawClass()));
                PropertyCustomizer propertyCustomizer = propertyCustomizers.get(propertyName);
                // here the recursion happens!
                buildSchemaFromTypeRecursively(member.getType(), propertyAnnotationsSupplier,
                        propertySchema -> schemaWithoutProperties.addProperty(propertyName,
                                propertyCustomizer.customize(propertySchema, member.getType(), propertyAnnotationsSupplier)),
                        recursionDepth + 1
                );
            });
        }

        private Map<String, PropertyCustomizer> customizeSchema(Schema schema, JavaType javaType, AnnotationsSupplier annotationsSupplier, Set<String> propertyNames) {
            Map<String, PropertyCustomizer> customizerProperties = buildStringMapFromStream(
                    propertyNames.stream(),
                    x -> x,
                    ignored -> new PropertyCustomizer()
            );
            schemaCustomizers.forEach(customizer -> customizer.customize(schema, javaType, annotationsSupplier, customizerProperties));
            return customizerProperties;
        }


        private InitialSchema buildSchemaFromTypeWithoutProperties(JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            InitialSchema initialSchema = getSchemaFromInitialTypeResolvers(javaType);

            // applying the schemaAnnotationMapper is treated specially here:
            // 1) It can only be built with an existing SchemaResolver (this class!)
            //    so that would end up in a circular loop if it wasn't resolved by using the schemaAnnotationMapperFactory
            // 2) Using it requires a referencedSchemaConsumer, something which is only present during building and not as a singleton
            annotationsSupplier.findAnnotations(io.swagger.v3.oas.annotations.media.Schema.class)
                    // apply in reverse order
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(schemaAnnotation -> schemaAnnotationMapper.applyFromAnnotation(initialSchema.getSchema(), schemaAnnotation, referencedSchemaConsumer));

            return initialSchema;
        }

        private InitialSchema getSchemaFromInitialTypeResolvers(JavaType javaType) {
            for (InitialTypeResolver initialTypeResolver : initialTypeResolvers) {
                InitialSchema initialSchema = initialTypeResolver.resolveFromType(javaType);
                if (initialSchema != null) {
                    return initialSchema;
                }
            }
            throw new IllegalStateException("No initial type resolver found for " + javaType);
        }

        void resolveReferencedSchemas() {
            defaultSchemaResolverSupport.forEach(referencedSchema -> {
                Schema schema = referencedSchema.getSchema();
                if (referencedSchema.mustBeReferenced()) {
                    // already set (not globally unique) reference here to have a "comparable" schema after this resolution
                    // globally unique reference identifier will be set after all schemas are collected
                    referencedSchema.consumeSchema(schema);
                    referencedSchemaConsumer.alwaysAsReference(schema,
                            // globally unique reference identifier is known finally, then set it at all places
                            referencedSchema::consumeSchema
                    );
                } else if (referencedSchema.isTopLevel()) {
                    // just one reference and on top-level,
                    // so let the caller decide if the final schema should or must be referenced
                    referencedSchema.consumeSchema(schema);
                } else {
                    // "maybeAsReference" sets the schema immediately by calling consumeSchema. This ensures that the returned schema
                    // is completely built before returning and allows proper "schema equality" comparisons later
                    // the consumeSchema might be called again if it was decided later that
                    // the schema is referenced instead of being inlined
                    referencedSchemaConsumer.maybeAsReference(schema, referencedSchema::consumeSchema);
                }
            });
        }
    }

    private static class PropertyCustomizer implements SchemaCustomizer.SchemaProperty {

        @Nullable
        private SchemaCustomizer.SchemaPropertyCustomizer schemaPropertyCustomizer;

        @Override
        public void customize(SchemaCustomizer.SchemaPropertyCustomizer propertySchemaCustomizer) {
            this.schemaPropertyCustomizer = propertySchemaCustomizer;
        }

        public Schema customize(Schema propertySchema, JavaType javaType, AnnotationsSupplier annotationsSupplier) {
            if (schemaPropertyCustomizer != null) {
                schemaPropertyCustomizer.customize(propertySchema, javaType, annotationsSupplier);
                // avoid running customizers multiple again when referenced schemas are consumed
                schemaPropertyCustomizer = null;
            }
            return propertySchema;
        }
    }
}
