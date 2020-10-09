package de.qaware.openapigeneratorforspring.common.operation.response;

import java.lang.reflect.Method;

public interface ApiResponseCodeMapper {
    String getFromMethod(Method method);
}
