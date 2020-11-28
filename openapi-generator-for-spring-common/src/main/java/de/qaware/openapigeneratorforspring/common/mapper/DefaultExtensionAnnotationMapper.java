package de.qaware.openapigeneratorforspring.common.mapper;

import de.qaware.openapigeneratorforspring.common.util.OpenApiMapUtils;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.prependIfMissing;

@RequiredArgsConstructor
public class DefaultExtensionAnnotationMapper implements ExtensionAnnotationMapper {

    protected static final String EXTENSION_PROPERTY_PREFIX = "x-";

    private final ParsableValueMapper parsableValueMapper;

    @Override
    public Map<String, Object> mapArray(Extension[] extensionAnnotations) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Extension extension : extensionAnnotations) {
            String name = extension.name();
            if (name.isEmpty()) {
                map.putAll(getPropertiesAsMap(extension.properties(), true));
            } else {
                String keyFromName = prependPrefix(name);
                map.put(keyFromName, getPropertiesAsMap(extension.properties(), false));
            }
        }
        return map;
    }

    private Map<String, Object> getPropertiesAsMap(ExtensionProperty[] properties, boolean prependPrefix) {
        return OpenApiMapUtils.buildStringMapFromStream(
                Stream.of(properties).filter(this::isNameAndValueNotBlank),
                property -> prependPrefix ? prependPrefix(property.name()) : property.name(),
                this::getPossiblyParsedPropertyValue
        );
    }

    private String prependPrefix(String name) {
        return prependIfMissing(name, EXTENSION_PROPERTY_PREFIX);
    }

    private Object getPossiblyParsedPropertyValue(ExtensionProperty property) {
        return property.parseValue() ? parsableValueMapper.parse(property.value()) : property.value();
    }

    private boolean isNameAndValueNotBlank(ExtensionProperty property) {
        return StringUtils.isNotBlank(property.name()) && StringUtils.isNotBlank(property.value());
    }
}
