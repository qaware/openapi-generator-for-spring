package de.qaware.openapigeneratorforspring.test.app41;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class App41Controller {

    @PostMapping
    public SomeType1 mapping1(@RequestBody SomeType1 requestBody) {
        return null;
    }

    @PutMapping
    public void mapping2(@RequestBody SomeType2 someType2) {

    }

    private static class SomeType2 {
        @SuppressWarnings("FieldCanBeLocal")
        private final String finalProperty;

        private SomeType2(
                @JsonProperty(value = "final_property", access = JsonProperty.Access.WRITE_ONLY)
                @Schema(description = "description for final property", implementation = Integer.class) String finalProperty
        ) {
            this.finalProperty = finalProperty;
        }
    }

    @JsonIgnoreProperties({"ignored_property"})
    private static class SomeType1 {
        @SuppressWarnings("FieldCanBeLocal")
        @JsonProperty(value = "write_only_property", access = JsonProperty.Access.WRITE_ONLY)
        @Schema(implementation = Integer.class)
        private String writeOnlyProperty;

        @JsonProperty(value = "write_only_property_field", access = JsonProperty.Access.WRITE_ONLY)
        @Schema(implementation = Integer.class)
        private String writeOnlyPropertyField;

        @JsonProperty(value = "read_only_property", access = JsonProperty.Access.READ_ONLY)
        @Schema(implementation = Integer.class)
        private String readOnlyProperty;

        @Getter
        @Setter
        @JsonProperty("ignored_property")
        private String ignoredProperty1;

        @JsonIgnore
        @Getter
        @Setter
        private String ignoredProperty2;

        @Schema(description = "description for read-only property")
        public String getReadOnlyProperty() {
            return readOnlyProperty;
        }

        @Schema(description = "description for write-only property")
        public void setWriteOnlyProperty(String writeOnlyProperty) {
            this.writeOnlyProperty = writeOnlyProperty;
        }
    }

}
