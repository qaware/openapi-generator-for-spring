package de.qaware.openapigeneratorforspring.common.schema.resolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class DefaultSchemaResolverTest {

    @Test
    public void name() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaType javaType1 = objectMapper.constructType(String.class);
        JavaType javaType2 = objectMapper.constructType(String.class);
        JavaType javaType3 = objectMapper.constructType(Class1.class);
        JavaType javaType4 = objectMapper.constructType(Class1.class);
        JavaType javaType5 = objectMapper.constructType(Class2.class);
//        JavaType javaType6 = objectMapper.constructType(TypeRef);

    }

    private static class Class1 {
        String property1;
    }


    private static class Class2<T> {
        String property2;
    }
}
