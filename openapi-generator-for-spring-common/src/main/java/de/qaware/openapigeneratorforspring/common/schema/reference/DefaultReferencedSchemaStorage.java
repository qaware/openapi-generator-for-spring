package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceDecider;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DefaultReferencedSchemaStorage implements ReferencedSchemaStorage {

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolver referenceNameConflictResolver;
    private final ReferenceDecider referenceDecider;

    private final List<SchemaWithSetters> entries = new ArrayList<>();

    @Override
    public void storeSchemaMaybeReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        SchemaWithSetters entry = getEntryOrAddNew(schema);
        entry.getReferenceNameOptionalSetters().add(referenceNameSetter);
    }

    @Override
    public void storeSchemaAlwaysReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        SchemaWithSetters entry = getEntryOrAddNew(schema);
        entry.getReferenceNameRequiredSetters().add(referenceNameSetter);
    }

    private SchemaWithSetters getEntryOrAddNew(Schema schema) {
        return entries.stream()
                .filter(entry -> Objects.equals(entry.getSchema().getName(), schema.getName()) && entry.getSchema().equals(schema))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a.schema + " vs. " + b.schema);
                })
                .orElseGet(() -> new SchemaWithSetters(schema)); // constructor registers it in entries as well
    }

    @Override
    public Map<ReferenceName, Schema> buildReferencedSchemas() {
        Map<ReferenceName, Schema> referencedSchemas = new HashMap<>();

        entries.stream()
                .collect(Collectors.groupingBy(entry -> referenceNameFactory.create(entry.getSchema())))
                .forEach((referenceName, groupedSchemasWithSetters) ->
                        buildUniqueReferenceNames(groupedSchemasWithSetters, referenceName).forEach(schemaWithSettersAndUniqueReferenceName -> {
                            ReferenceName uniqueReferenceName = schemaWithSettersAndUniqueReferenceName.getUniqueReferenceName();
                            SchemaWithSetters schemaWithSetters = schemaWithSettersAndUniqueReferenceName.getSchemaWithSetters();
                            Schema schema = schemaWithSetters.getSchema();

                            List<Consumer<ReferenceName>> requiredSetters = schemaWithSetters.getReferenceNameRequiredSetters();
                            List<Consumer<ReferenceName>> optionalSetters = schemaWithSetters.getReferenceNameOptionalSetters();

                            if (!requiredSetters.isEmpty()) {
                                // we're required to set reference names
                                requiredSetters.forEach(requiredSetter -> requiredSetter.accept(uniqueReferenceName));
                                // then also propagate that reference to the optional setters (if any)
                                optionalSetters.forEach(optionalSetter -> optionalSetter.accept(uniqueReferenceName));
                                addToReferencedSchemas(referencedSchemas, uniqueReferenceName, schema);
                            } else {
                                // required setters are empty, so we may reference it here -> ask decider about it
                                // note that optionalSetters are never empty here, as both "store" methods add at least one entry
                                ReferenceName maybeReferenceName = referenceDecider.decide(schema, optionalSetters.size(), uniqueReferenceName);
                                if (maybeReferenceName != null) {
                                    optionalSetters.forEach(optionalSetter -> optionalSetter.accept(maybeReferenceName));
                                    addToReferencedSchemas(referencedSchemas, maybeReferenceName, schema);
                                }
                            }
                        }));
        return referencedSchemas;
    }

    private void addToReferencedSchemas(Map<ReferenceName, Schema> referencedSchemas, ReferenceName referenceName, Schema schema) {
        referencedSchemas.merge(referenceName, schema, (a, b) -> {
            throw new IllegalStateException("Encountered conflicting reference name " + referenceName + ": " + a + " vs. " + b);
        });
    }

    private Stream<SchemaWithSettersAndUniqueReferenceName> buildUniqueReferenceNames(List<SchemaWithSetters> schemasWithSetters, ReferenceName referenceName) {
        if (schemasWithSetters.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one schema for this reference name
            return Stream.of(SchemaWithSettersAndUniqueReferenceName.of(referenceName, schemasWithSetters.get(0)));
        }

        List<Schema> schemasWithSameReferenceName = schemasWithSetters.stream()
                .map(SchemaWithSetters::getSchema)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(schemasWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != schemasWithSameReferenceName.size()) {
            // TODO improve error message information here
            throw new IllegalStateException("The reference name conflict resolver did not return expected number of unique reference names");
        }
        if (new HashSet<>(uniqueReferenceNames).size() != uniqueReferenceNames.size()) {
            throw new IllegalStateException("Reference names from conflict resolver are not unique: " + uniqueReferenceNames);
        }

        // zip those those arrays
        return IntStream.range(0, schemasWithSetters.size()).boxed()
                .map(i -> SchemaWithSettersAndUniqueReferenceName.of(uniqueReferenceNames.get(i), schemasWithSetters.get(i)));
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class SchemaWithSettersAndUniqueReferenceName {
        private final ReferenceName uniqueReferenceName;
        private final SchemaWithSetters schemaWithSetters;
    }

    @Getter
    private class SchemaWithSetters {

        private final Schema schema;
        private final List<Consumer<ReferenceName>> referenceNameRequiredSetters = new ArrayList<>();
        private final List<Consumer<ReferenceName>> referenceNameOptionalSetters = new ArrayList<>();

        public SchemaWithSetters(Schema schema) {
            this.schema = schema;
            // register instance in parent class
            entries.add(this);
        }

    }


}
