package de.qaware.openapigeneratorforspring.common.util;

import de.qaware.openapigeneratorforspring.model.media.Schema;
import de.qaware.openapigeneratorforspring.model.operation.Operation;
import de.qaware.openapigeneratorforspring.model.parameter.Parameter;
import de.qaware.openapigeneratorforspring.model.response.ApiResponse;
import de.qaware.openapigeneratorforspring.model.response.ApiResponses;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiProxyUtilsTest {

    @Test
    void immutableProxy() throws Exception {
        Schema schema = new Schema();
        schema.setRef("ref");

        Schema immutableSchema = OpenApiProxyUtils.immutableProxy(schema);
        immutableSchema.setRef("newRef");
        immutableSchema.setNullable(true);

        assertThat(immutableSchema.getRef()).isEqualTo("ref");
        assertThat(immutableSchema.getNullable()).isNull();
        assertThat(schema.getRef()).isEqualTo("ref");
        assertThat(schema.getNullable()).isNull();
    }

    @Test
    void smartImmutableProxy_simpleValues() {
        Parameter parameter = new Parameter();
        parameter.setIn("initial");

        Parameter immutableParameter = OpenApiProxyUtils.smartImmutableProxy(parameter);
        immutableParameter.setIn("changed");
        immutableParameter.setRequired(true);
        immutableParameter.setRequired(false); // stays true

        assertThat(parameter.getIn()).isEqualTo("initial");
        assertThat(parameter.getRequired()).isTrue();
    }

    @Test
    void smartImmutableProxy_simpleMap() {
        Parameter parameter = new Parameter();
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("key1", "value1");
        parameter.setExtensions(extensions);

        Parameter immutableParameter = OpenApiProxyUtils.smartImmutableProxy(parameter, OpenApiProxyUtils::addNonExistingKeys);
        immutableParameter.setExtensions(Collections.singletonMap("key1", true)); // ignored
        immutableParameter.setExtensions(Collections.singletonMap("key2", true)); // ok

        assertThat(parameter.getExtensions()).containsEntry("key1", "value1");
        assertThat(parameter.getExtensions()).containsEntry("key2", true);
    }

    @Test
    void smartImmutableProxy_derivedMap() {
        Operation operation = new Operation();
        ApiResponses responses = new ApiResponses();
        ApiResponse value1 = new ApiResponse();
        value1.setDescription("value1");
        responses.put("key1", value1);
        operation.setResponses(responses);

        Operation immutableOperation = OpenApiProxyUtils.smartImmutableProxy(operation, OpenApiProxyUtils::addNonExistingKeys);
        ApiResponses newResponses = new ApiResponses();
        ApiResponse value2 = new ApiResponse();
        value2.setDescription("value2");
        newResponses.put("key1", new ApiResponse());
        newResponses.put("key2", value2);
        immutableOperation.setResponses(newResponses);

        assertThat(operation.getResponses().get("key1")).isSameAs(value1);
        assertThat(operation.getResponses().get("key2")).isSameAs(value2);
    }

}
