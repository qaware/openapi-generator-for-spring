package de.qaware.openapigeneratorforspring.common.schema;

import de.qaware.openapigeneratorforspring.common.reference.ReferenceName;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameConflictResolver;
import de.qaware.openapigeneratorforspring.common.reference.ReferenceNameFactory;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultReferencedSchemaStorage implements ReferencedSchemaStorage {

    private final ReferenceNameFactory referenceNameFactory;
    private final ReferenceNameConflictResolver referenceNameConflictResolver;

    private final List<SchemaWithReferenceNameSetters> entries = new ArrayList<>();

    @Override
    public void storeSchema(Schema schema, Consumer<ReferenceName> referenceNameSetter) {
        SchemaWithReferenceNameSetters entry = getEntryOrAddNew(schema);
        entry.getReferenceNameSetters().add(referenceNameSetter);
    }

    private SchemaWithReferenceNameSetters getEntryOrAddNew(Schema schema) {
        return entries.stream()
                .filter(entry -> Objects.equals(entry.getSchema().getName(), schema.getName()) && entry.getSchema().equals(schema))
                .findAny()
                .orElseGet(() -> {
                    SchemaWithReferenceNameSetters entry = new SchemaWithReferenceNameSetters(schema);
                    entries.add(entry);
                    return entry;
                });
    }

    @Override
    public Map<ReferenceName, Schema> buildReferencedSchemas() {
        Map<ReferenceName, Schema> referencedSchemas = new HashMap<>();

        Map<ReferenceName, List<SchemaWithReferenceNameSetters>> entriesGroupedByReferenceName = entries.stream()
                .collect(Collectors.groupingBy(entry -> referenceNameFactory.create(entry.getSchema())));
        for (Map.Entry<ReferenceName, List<SchemaWithReferenceNameSetters>> groupedEntry : entriesGroupedByReferenceName.entrySet()) {
            ReferenceName referenceName = groupedEntry.getKey();
            List<SchemaWithReferenceNameSetters> schemasWithSetters = groupedEntry.getValue();
            List<ReferenceName> uniqueReferenceNames = getUniqueReferenceNames(schemasWithSetters, referenceName);
            for (int i = 0; i < uniqueReferenceNames.size(); i++) {
                ReferenceName uniqueReferenceName = uniqueReferenceNames.get(i);
                SchemaWithReferenceNameSetters schemaWithSetters = schemasWithSetters.get(i);

                Schema existingSchema = referencedSchemas.get(uniqueReferenceName);
                if (existingSchema != null) {
                    throw new IllegalStateException("Encountered conflicting reference name " + uniqueReferenceName
                            + " for " + existingSchema + " vs. " + schemaWithSetters.getSchema());
                }

                schemaWithSetters.getReferenceNameSetters()
                        .forEach(referenceNameSetter -> referenceNameSetter.accept(uniqueReferenceName));

                referencedSchemas.put(uniqueReferenceName, schemaWithSetters.getSchema());
            }

        }
        return referencedSchemas;
    }

    private List<ReferenceName> getUniqueReferenceNames(List<SchemaWithReferenceNameSetters> schemasWithSetters, ReferenceName referenceName) {
        if (schemasWithSetters.size() == 1) {
            return Collections.singletonList(referenceName);
        }

        List<Schema> schemasWithSameReferenceName = schemasWithSetters.stream()
                .map(SchemaWithReferenceNameSetters::getSchema)
                .collect(Collectors.toList());

        List<ReferenceName> uniqueReferenceNames = referenceNameConflictResolver.resolveConflict(schemasWithSameReferenceName, referenceName);
        if (uniqueReferenceNames.size() != schemasWithSameReferenceName.size()) {
            // TODO improve error message information here
            throw new IllegalStateException("The reference name conflict resolver did not return expected number of unique reference names");
        }
        if (new HashSet<>(uniqueReferenceNames).size() != uniqueReferenceNames.size()) {
            throw new IllegalStateException("Reference names from conflict resolver are not unique: " + uniqueReferenceNames);
        }
        return uniqueReferenceNames;
    }


    @Value
    private static class SchemaWithReferenceNameSetters {
        de.qaware.openapigeneratorforspring.common.schema.Schema schema;
        List<Consumer<ReferenceName>> referenceNameSetters = new ArrayList<>();
    }


}
