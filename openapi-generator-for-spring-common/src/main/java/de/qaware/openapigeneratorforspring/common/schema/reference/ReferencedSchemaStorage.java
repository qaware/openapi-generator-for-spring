package de.qaware.openapigeneratorforspring.common.schema.reference;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import de.qaware.openapigeneratorforspring.common.schema.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ReferencedSchemaStorage {

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolverForSchema referenceNameConflictResolver;
    private final ReferenceDeciderForSchema referenceDecider;

    private final List<SchemaWithSetters> entries = new ArrayList<>();

    public void storeSchemaMaybeReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).getReferenceNameOptionalSetters().add(referenceNameSetter);
    }

    public void storeSchemaAlwaysReference(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        getEntryOrAddNew(schema).getReferenceNameRequiredSetters().add(referenceNameSetter);
    }

    private SchemaWithSetters getEntryOrAddNew(Schema schema) {
        return entries.stream()
                .filter(entry -> Objects.equals(entry.getSchema().getName(), schema.getName()) && entry.getSchema().equals(schema))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Found more than one entry: " + a.schema + " vs. " + b.schema);
                })
                .orElseGet(() -> new SchemaWithSetters(schema)); // constructor registers it in entries as well
    }

    public Map<String, Schema> buildReferencedSchemas() {
        Map<String, Schema> referencedSchemas = new HashMap<>();
        entries.stream()
                .filter(entry -> {
                    if (entry.getReferenceNameRequiredSetters().isEmpty()) {
                        // note that optionalSetters are never empty here, as both "store" methods add at least one entry
                        return referenceDecider.turnIntoReference(entry.getSchema(), entry.getReferenceNameOptionalSetters().size());
                    }
                    // we have required setters, so always reference the item
                    return true;
                })
                .collect(Collectors.groupingBy(entry -> referenceNameFactory.create(entry.getSchema(), entry.getSchema().getName())))
                .forEach((referenceName, groupedSchemasWithSetters) ->
                        buildUniqueReferenceNames(groupedSchemasWithSetters, referenceName).forEach((uniqueReferenceName, schemaWithSetters) -> {
                            Schema schema = schemaWithSetters.getSchema();
                            List<Consumer<ReferenceName>> requiredSetters = schemaWithSetters.getReferenceNameRequiredSetters();
                            List<Consumer<ReferenceName>> optionalSetters = schemaWithSetters.getReferenceNameOptionalSetters();
                            requiredSetters.forEach(requiredSetter -> requiredSetter.accept(uniqueReferenceName));
                            optionalSetters.forEach(optionalSetter -> optionalSetter.accept(uniqueReferenceName));
                            referencedSchemas.put(uniqueReferenceName.getIdentifier(), schema);
                        }));
        return referencedSchemas;
    }

    private Map<ReferenceName, SchemaWithSetters> buildUniqueReferenceNames(List<SchemaWithSetters> schemasWithSetters, ReferenceName referenceName) {
        if (schemasWithSetters.size() == 1) {
            // special case: no conflicts need to be resolved as there's only one schema for this reference name
            return Collections.singletonMap(referenceName, schemasWithSetters.get(0));
        }

        List<Schema> schemasWithSameReferenceName = schemasWithSetters.stream()
                .map(SchemaWithSetters::getSchema)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(schemasWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != schemasWithSameReferenceName.size()) {
            // TODO improve error message information here
            throw new IllegalStateException("The reference name conflict resolver did not return expected number of unique reference names");
        }

        // zip those those arrays into map
        return IntStream.range(0, schemasWithSetters.size()).boxed()
                .collect(Collectors.toMap(uniqueReferenceNames::get, schemasWithSetters::get, (a, b) -> {
                    throw new IllegalStateException("Found non-unique reference name from conflict resolver: " + a + " vs. " + b);
                }));
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
