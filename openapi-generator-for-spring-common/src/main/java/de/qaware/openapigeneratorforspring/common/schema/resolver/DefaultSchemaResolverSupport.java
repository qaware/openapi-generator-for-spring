package de.qaware.openapigeneratorforspring.common.schema.resolver;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Internal support for {@link DefaultSchemaResolver}. Tracks the
 * {@link #referencedSchemas} during recursive analysis.
 */
class DefaultSchemaResolverSupport {

    @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    static class ReferencedSchema {
        @Getter
        private final Schema schema;
        @Getter
        private final boolean topLevel;
        private final Set<String> propertyNames;
        @Getter(AccessLevel.PRIVATE)
        private final LinkedList<Consumer<Schema>> referenceConsumers;

        boolean mustBeReferenced() {
            // this is not the optimal way of deciding if something must be referenced,
            // but it's good enough to handle self-referencing types
            // (although they're not really useful, it's cool to support them)
            return referenceConsumers.size() > 1 && !propertyNames.isEmpty();
        }

        void consumeSchema(Schema newSchema) {
            // as schemas are referenced and the storage compares schema including properties,
            // it's important to run the consumers such that the last encountered consumer is accepted first
            referenceConsumers.descendingIterator().forEachRemaining(schemaConsumer -> schemaConsumer.accept(newSchema));
        }

        public boolean matches(Schema schema, Set<String> propertyNames) {
            return this.schema.equals(schema) && this.propertyNames.equals(propertyNames);
        }
    }

    private final LinkedList<ReferencedSchema> referencedSchemas = new LinkedList<>();

    void forEach(Consumer<ReferencedSchema> action) {
        // as schemas are referenced and the storage compares schema including properties,
        // it's important to iterate over the items in reversed order
        referencedSchemas.descendingIterator().forEachRemaining(referencedSchema -> {
            if (referencedSchema.schema.isEmpty()) {
                throw new IllegalStateException("Encountered completely empty schema");
            }
            if (referencedSchema.referenceConsumers.isEmpty()) {
                throw new IllegalStateException("Encountered schema without any schema consumers, that's strange");
            }
            action.accept(referencedSchema);
        });
    }

    void consumeSchemaWithProperties(Schema schemaWithoutProperties, Set<String> propertyNames, boolean isTopLevel,
                                     Consumer<Schema> schemaConsumer, Runnable onNotFound) {
        List<Consumer<Schema>> schemaReferenceSetters = findSchemaReferenceSetters(schemaWithoutProperties, propertyNames);
        if (schemaReferenceSetters != null) {
            // we've seen this schema with its property names before, then simply reference it lazily
            schemaReferenceSetters.add(schemaConsumer);
            return;
        }

        // important to add the schemaWithoutProperties first before traversing the nested properties (if any)
        // this prevents infinite loops when types refer to themselves
        referencedSchemas.add(ReferencedSchema.of(
                schemaWithoutProperties,
                isTopLevel,
                propertyNames,
                new LinkedList<>(Collections.singleton(schemaConsumer))
        ));

        onNotFound.run();
    }

    @Nullable
    private List<Consumer<Schema>> findSchemaReferenceSetters(Schema schema, Set<String> propertyNames) {
        ensurePropertiesAreNowhereSet(schema);

        if (schema.isEmpty() && propertyNames.isEmpty()) {
            throw new IllegalStateException("Schema is completely empty. This is not supported here.");
        }

        return referencedSchemas.stream()
                .filter(referencedSchema -> referencedSchema.matches(schema, propertyNames))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found two entries for " + schema + ": " + a + " vs. " + b);
                })
                .map(ReferencedSchema::getReferenceConsumers)
                .orElse(null);
    }

    private void ensurePropertiesAreNowhereSet(Schema schema) {
        // safe-guard against wrong implementation of GenericTypeResolvers
        // they must defer setting properties until resolveReferencedSchemas is called
        if (schema.getProperties() != null) {
            throw new IllegalStateException("To be added schema has non-null properties");
        }
        referencedSchemas.stream().map(ReferencedSchema::getSchema).forEach(existingSchema -> {
            if (existingSchema.getProperties() != null) {
                throw new IllegalStateException("Existing referenced schema has non-null properties");
            }
        });
    }


}
