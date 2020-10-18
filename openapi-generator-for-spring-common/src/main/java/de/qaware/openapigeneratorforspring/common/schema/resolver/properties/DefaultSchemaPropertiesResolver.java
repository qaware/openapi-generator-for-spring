package de.qaware.openapigeneratorforspring.common.schema.resolver.properties;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultSchemaPropertiesResolver implements SchemaPropertiesResolver {

    private final OpenApiObjectMapperSupplier objectMapperSupplier;
    private final List<SchemaPropertyFilter> propertyFilters;

    @Override
    public Map<String, AnnotatedMember> findProperties(JavaType javaType) {
        BeanDescription beanDescriptionForType = objectMapperSupplier.get().getSerializationConfig().introspect(javaType);
        return OpenApiMapUtils.buildStringMapFromStream(
                beanDescriptionForType.findProperties().stream().filter(property -> propertyFilters.stream().allMatch(filter -> filter.accept(property, beanDescriptionForType))),
                BeanPropertyDefinition::getName,
                BeanPropertyDefinition::getAccessor
        );
    }
}
