package de.qaware.openapigeneratorforspring.model.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.qaware.openapigeneratorforspring.model.ExternalDocumentation;
import de.qaware.openapigeneratorforspring.model.extension.HasExtensions;
import de.qaware.openapigeneratorforspring.model.reference.HasReference;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Schema
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#schemaObject"
 */
@Data
@Builder
public class Schema implements HasExtensions, HasReference {
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private String name;

    @JsonProperty("default")
    private Object _default;
    private String title;
    private BigDecimal multipleOf;
    private BigDecimal maximum;
    private Boolean exclusiveMaximum;
    private BigDecimal minimum;
    private Boolean exclusiveMinimum;
    private Integer maxLength;
    private Integer minLength;
    private String pattern;
    private Integer maxItems;
    private Integer minItems;
    private Boolean uniqueItems;
    private Integer maxProperties;
    private Integer minProperties;
    private List<String> required;
    @With
    private String type;
    private Schema not;
    private Map<String, Schema> properties;
    private Object additionalProperties;
    private String description;
    private String format;
    private String ref;
    private Boolean nullable;
    private Boolean readOnly;
    private Boolean writeOnly;
    private Object example;
    private ExternalDocumentation externalDocs;
    private Boolean deprecated;
    private XML xml;
    @JsonProperty("enum")
    private List<Object> _enum;
    private Discriminator discriminator;
    private Schema items;
    private Map<String, Object> extensions;

    public void addProperty(String propertyName, Schema propertySchema) {
        if (properties == null) {
            properties = new LinkedHashMap<>();
        }
        properties.put(propertyName, propertySchema);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return builder().build().equals(this);
    }
}

