package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultSchemaPropertiesResolver implements SchemaPropertiesResolver {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;
    private final List<SchemaPropertyFilter> propertyFilters;

    @Override
    public Map<String, AnnotatedMember> findProperties(JavaType javaType) {
        BeanDescription beanDescriptionForType = objectMapperSupplier.get().getSerializationConfig().introspect(javaType);
        return beanDescriptionForType.findProperties().stream()
                .filter(property -> Objects.nonNull(property.getAccessor()))    // safe-guard weird properties
                .filter(property -> StringUtils.isNotBlank(property.getName())) // safe-guard weird properties
                .filter(property -> propertyFilters.stream().allMatch(filter -> filter.accept(property, beanDescriptionForType)))
                .collect(Collectors.toMap(BeanPropertyDefinition::getName, BeanPropertyDefinition::getAccessor));
    }
}
