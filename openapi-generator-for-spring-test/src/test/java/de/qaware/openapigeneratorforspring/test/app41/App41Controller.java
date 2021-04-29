package de.qaware.openapigeneratorforspring.test.app41;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class App41Controller {

    @PostMapping
    public SomeType mapping1(@RequestBody SomeType requestBody) {
        return null;
    }

    private static class SomeType {
        @SuppressWarnings("FieldCanBeLocal")
        @JsonProperty(value = "write_only_property", access = JsonProperty.Access.WRITE_ONLY)
        @Schema(implementation = Integer.class)
        private String writeOnlyProperty;

        @JsonProperty(value = "read_only_property", access = JsonProperty.Access.READ_ONLY)
        @Schema(implementation = Integer.class)
        private String readOnlyProperty;

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
