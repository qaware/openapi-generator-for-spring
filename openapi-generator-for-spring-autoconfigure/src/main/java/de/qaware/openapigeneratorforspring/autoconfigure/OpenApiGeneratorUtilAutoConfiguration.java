package de.qaware.openapigeneratorforspring.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.qaware.openapigeneratorforspring.common.util.OpenApiObjectMapperSupplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

public class OpenApiGeneratorUtilAutoConfiguration {
    public static final String EXTENSIONS_PROPERTY_NAME = "extensions";
    // TODO consider this choice again:
    // Maybe the "auto-configured" object mapper from spring would work better?
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // TODO replace by self-owned model and @JsonAnyGetter annotation on extensions map
            .registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    super.setupModule(context);
                    context.addBeanSerializerModifier(new BeanSerializerModifier() {
                        @Override
                        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                            for (int i = 0; i < beanProperties.size(); ++i) {
                                BeanPropertyWriter writer = beanProperties.get(i);
                                if (EXTENSIONS_PROPERTY_NAME.equals(writer.getName())) {
                                    beanProperties.set(i, new ExtensionsPropertyWriter(writer));
                                }
                            }
                            return beanProperties;
                        }
                    });
                }
            })
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());


    @Bean
    @ConditionalOnMissingBean
    public OpenApiObjectMapperSupplier defaultOpenApiObjectMapperSupplier() {
        return () -> OBJECT_MAPPER;
    }

    private static class ExtensionsPropertyWriter extends BeanPropertyWriter {

        protected ExtensionsPropertyWriter(BeanPropertyWriter base) {
            super(base);
        }


        @Override
        public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
            // first part is copied from overridden method

            // inlined 'get()'
            final Object value = (_accessorMethod == null) ? _field.get(bean)
                    : _accessorMethod.invoke(bean, (Object[]) null);

            // Null handling is bit different, check that first
            if (value == null) {
                if (_nullSerializer != null) {
                    gen.writeFieldName(_name);
                    _nullSerializer.serialize(null, gen, prov);
                }
                return;
            }
            // then find serializer to use
            JsonSerializer<?> ser = _serializer;
            if (ser == null) {
                Class<?> cls = value.getClass();
                PropertySerializerMap m = _dynamicSerializers;
                ser = m.serializerFor(cls);
                if (ser == null) {
                    ser = _findAndAddDynamic(m, cls, prov);
                }
            }

            // copied from com.fasterxml.jackson.databind.ser.AnyGetterWriter
            if (ser instanceof MapSerializer) {
                MapSerializer mapSerializer = (MapSerializer) ser;
                mapSerializer.serializeWithoutTypeInfo((Map<?, ?>) value, gen, prov);
            }
        }
    }
}
