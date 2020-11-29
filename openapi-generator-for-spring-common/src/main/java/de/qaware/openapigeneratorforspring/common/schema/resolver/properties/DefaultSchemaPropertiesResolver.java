package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.supplier.OpenApiObjectMapperSupplier.Purpose.SCHEMA_BUILDING;

@RequiredArgsConstructor
public class DefaultSchemaPropertiesResolver implements SchemaPropertiesResolver {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;
    private final List<SchemaPropertyFilter> propertyFilters;

    @Override
    public Map<String, AnnotatedMember> findProperties(JavaType javaType) {
        BeanDescription beanDescriptionForType = objectMapperSupplier.get(SCHEMA_BUILDING).getSerializationConfig().introspect(javaType);
        return OpenApiMapUtils.buildStringMapFromStream(
                beanDescriptionForType.findProperties().stream().filter(property -> propertyFilters.stream().allMatch(filter -> filter.accept(property, beanDescriptionForType))),
                BeanPropertyDefinition::getName,
                BeanPropertyDefinition::getAccessor
        );
    }
}
