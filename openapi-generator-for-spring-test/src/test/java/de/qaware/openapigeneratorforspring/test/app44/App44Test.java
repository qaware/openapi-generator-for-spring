package de.qaware.openapigeneratorforspring.test.app44;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.test.AbstractOpenApiGeneratorWebMvcIntTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static de.qaware.openapigeneratorforspring.common.schema.resolver.type.extension.jackson.DefaultJacksonPolymorphismTypeSchemaNameBuilder.SCHEMA_NAME_SUFFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class App44Test extends AbstractOpenApiGeneratorWebMvcIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ensureJacksonTypeNamesAreEqualWithSpec() throws Exception {
        String specJson = performApiDocsRequest(x -> x)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> serializedSpecJson = objectMapper.readValue(specJson, new TypeReference<Map<String, Object>>() {
        });
        Object typeEnumObject = getAsMap(getAsMap(getAsMap(serializedSpecJson, "components"), "schemas"), "Base1" + SCHEMA_NAME_SUFFIX).get("enum");

        String json = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Map<String, Object>> serializedJson = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });

        assertThat(typeEnumObject).isInstanceOfSatisfying(List.class, typeEnumValues -> {
            assertThat(typeEnumValues).isNotEmpty();
            assertThat(serializedJson.stream().map(m -> m.get("@c"))).containsExactlyInAnyOrderElementsOf(typeEnumValues);
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getAsMap(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.get(key);
    }
}
