package de.qaware.openapigeneratorforspring.common.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultExtensionAnnotationMapper implements ExtensionAnnotationMapper {

    protected static final String EXTENSION_PROPERTY_PREFIX = "x-";

    private final ParsableValueMapper parsableValueMapper;

    @Override
    public Map<String, Object> mapArray(Extension[] extensionAnnotations) {
        final Map<String, Object> map = new HashMap<>();
        for (Extension extension : extensionAnnotations) {
            final String name = extension.name();
            final String key = name.length() > 0 ? StringUtils.prependIfMissing(name, EXTENSION_PROPERTY_PREFIX) : name;

            for (ExtensionProperty property : extension.properties()) {
                String propertyName = property.name();
                String propertyValue = property.value();

                boolean propertyAsJson = property.parseValue();
                if (StringUtils.isNotBlank(propertyName) && StringUtils.isNotBlank(propertyValue)) {
                    if (key.isEmpty()) {
                        String prefixedKey = StringUtils.prependIfMissing(propertyName, EXTENSION_PROPERTY_PREFIX);
                        if (propertyAsJson) {
                            try {
                                JsonNode processedValue = Json.mapper().readTree(propertyValue);
                                map.put(prefixedKey, processedValue);
                            } catch (Exception e) {
                                map.put(prefixedKey, propertyValue);
                            }
                        } else {
                            map.put(prefixedKey, propertyValue);
                        }
                    } else {
                        Object value = map.get(key);
                        if (!(value instanceof Map)) {
                            value = new HashMap<String, Object>();
                            map.put(key, value);
                        }
                        @SuppressWarnings("unchecked") Map<String, Object> mapValue = (Map<String, Object>) value;
                        if (propertyAsJson) {
                            try {
                                JsonNode processedValue = Json.mapper().readTree(propertyValue);
                                mapValue.put(propertyName, processedValue);
                            } catch (Exception e) {
                                mapValue.put(propertyName, propertyValue);
                            }
                        } else {
                            mapValue.put(propertyName, propertyValue);
                        }
                    }
                }
            }
        }

        return map;
    }
}
